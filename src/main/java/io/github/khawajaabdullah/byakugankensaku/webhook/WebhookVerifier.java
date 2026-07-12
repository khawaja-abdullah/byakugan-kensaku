package io.github.khawajaabdullah.byakugankensaku.webhook;

import io.github.khawajaabdullah.byakugankensaku.util.HmacAlgorithm;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class WebhookVerifier {

  private WebhookVerifier() {
  }

  public static void verify(String signature, HmacAlgorithm hmacAlgorithm, String secret, String payload) {
    if (StringUtils.isBlank(secret)) {
      throw new IllegalArgumentException("Secret cannot be null or empty");
    }
    if (StringUtils.isBlank(signature)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing signature");
    }
    String[] signatureParts = signature.split("=");
    if (signatureParts.length != 2 || StringUtils.isBlank(signatureParts[0]) || StringUtils.isBlank(signatureParts[1])) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature format");
    }
    if (!hmacAlgorithm.getShortName().equals(signatureParts[0])) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature algorithm");
    }
    byte[] providedDigest = signatureParts[1].getBytes(StandardCharsets.UTF_8);
    byte[] expectedDigest = new HmacUtils(hmacAlgorithm.getName(), secret).hmacHex(payload).getBytes(StandardCharsets.UTF_8);
    if (!MessageDigest.isEqual(providedDigest, expectedDigest)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature");
    }
  }

}
