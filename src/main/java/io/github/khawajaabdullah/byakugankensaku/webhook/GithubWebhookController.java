package io.github.khawajaabdullah.byakugankensaku.webhook;

import io.github.khawajaabdullah.byakugankensaku.util.HmacAlgorithm;
import io.github.khawajaabdullah.byakugankensaku.util.WebhookDigestVerifier;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/github")
@RestController
public class GithubWebhookController {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GithubWebhookController.class);

  @Value("${app.github.webhook.secret}")
  String githubWebhookSecret;

  @PostMapping("/webhook")
  public void handleWebhook(@RequestHeader("X-Github-Hook-Id") String githubHookId,
                            @RequestHeader("X-GitHub-Event") String githubEvent,
                            @RequestHeader("X-Hub-Signature-256") String hubSignature256,
                            @RequestBody String payload) {
    WebhookDigestVerifier.verify(hubSignature256, HmacAlgorithm.HMAC_SHA_256, githubWebhookSecret, payload);
    LOGGER.info("Webhook received:- id: {}, event: {}, payload: {}", githubHookId, githubEvent, payload);
  }

}
