package io.github.khawajaabdullah.byakugankensaku.util;

import io.github.khawajaabdullah.byakugankensaku.exception.ByakuganKensakuException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

import static io.github.khawajaabdullah.byakugankensaku.util.Constant.ASSIGNMENT_OPERATOR;

@Slf4j
public final class HmacDigestVerifier {

  private HmacDigestVerifier() {
  }

  public static void verify(String signature, HmacAlgorithm hmacAlgorithm, String secret, String payload) {
    Objects.requireNonNull(secret, "Secret must not be null");
    Objects.requireNonNull(payload, "Payload must not be null");
    if (secret.isBlank()) {
      throw new ByakuganKensakuException("Secret cannot be blank");
    }
    if (signature == null || signature.isBlank()) {
      log.debug("Missing signature header");
      throw new ByakuganKensakuException("Missing signature");
    }

    SignatureParts parts = parseSignature(signature);

    if (!hmacAlgorithm.getShortName().equalsIgnoreCase(parts.algorithm)) {
      log.debug("Algorithm mismatch: expected '{}', got '{}'", hmacAlgorithm.getShortName(), parts.algorithm);
      throw new ByakuganKensakuException("Algorithm mismatch");
    }

    String expectedHex = new HmacUtils(hmacAlgorithm.getName(), secret).hmacHex(payload);
    byte[] providedBytes = parts.hexValue.getBytes(StandardCharsets.UTF_8);
    byte[] expectedBytes = expectedHex.getBytes(StandardCharsets.UTF_8);

    if (!MessageDigest.isEqual(providedBytes, expectedBytes)) {
      log.debug("Signature verification failed");
      throw new ByakuganKensakuException("Invalid signature");
    }
  }

  private static SignatureParts parseSignature(String signature) {
    String[] parts = signature.split(ASSIGNMENT_OPERATOR);
    if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
      log.debug("Invalid signature format: '{}'", signature);
      throw new ByakuganKensakuException("Invalid signature format");
    }
    return new SignatureParts(parts[0].trim(), parts[1].trim());
  }

  private record SignatureParts(String algorithm, String hexValue) {
  }

}
