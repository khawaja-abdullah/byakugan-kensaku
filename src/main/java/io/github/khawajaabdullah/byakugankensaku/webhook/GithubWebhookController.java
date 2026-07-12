package io.github.khawajaabdullah.byakugankensaku.webhook;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/github")
@RestController
public class GithubWebhookController {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GithubWebhookController.class);

  private final GithubWebhookVerifier githubWebhookVerifier;

  public GithubWebhookController(GithubWebhookVerifier githubWebhookVerifier) {
    this.githubWebhookVerifier = githubWebhookVerifier;
  }

  @PostMapping("/webhook")
  public void handleWebhook(@RequestHeader("X-Github-Hook-Id") String githubHookId,
                            @RequestHeader("X-GitHub-Event") String githubEvent,
                            @RequestHeader("X-Hub-Signature-256") String hubSignature256,
                            @RequestBody String payload) {
    githubWebhookVerifier.verify(hubSignature256, payload);
    LOGGER.info("Webhook received:- id: {}, event: {}, payload: {}", githubHookId, githubEvent, payload);
  }

}
