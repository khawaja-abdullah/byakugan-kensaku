package io.github.khawajaabdullah.byakugankensaku.webhook;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;

@Component
public class GithubWebhookVerifier {

  @Value("${github.webhook.secret}")
  private String webhookSecret;

  public void verify(String signature, String payload) {
    if (signature == null || !signature.startsWith("sha256=")) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid signature");
    }
    String actualDigest = signature.substring(7);
    String expectedDigest = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, webhookSecret).hmacHex(payload);
    if (!MessageDigest.isEqual(actualDigest.getBytes(), expectedDigest.getBytes())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature");
    }
  }

}
