package io.github.khawajaabdullah.byakugankensaku.util;

import io.micrometer.common.util.StringUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class WebhookDigestVerifier {

  private static final Charset UTF_8 = StandardCharsets.UTF_8;
  private static final String ASSIGNMENT_OPERATOR = "=";

  private WebhookDigestVerifier() {
  }

  public static void verify(String signature, HmacAlgorithm hmacAlgorithm, String secret, String payload) {
    if (StringUtils.isBlank(secret)) {
      throw new IllegalArgumentException("Secret cannot be null or empty");
    }

    if (StringUtils.isBlank(signature)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing signature");
    }

    String[] parts = signature.split(ASSIGNMENT_OPERATOR);
    if (parts.length != 2 || StringUtils.isBlank(parts[0]) || StringUtils.isBlank(parts[1])) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature format");
    }

    String providedPrefix = parts[0];
    String providedHex = parts[1];

    if (!hmacAlgorithm.getShortName().equalsIgnoreCase(providedPrefix)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
          "Algorithm mismatch. Expected " + hmacAlgorithm.getShortName() + " but got " + providedPrefix);
    }

    String expectedHex = new HmacUtils(hmacAlgorithm.getName(), secret).hmacHex(payload);

    if (!MessageDigest.isEqual(providedHex.getBytes(UTF_8), expectedHex.getBytes(UTF_8))) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature");
    }
  }

}
