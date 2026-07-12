package io.github.khawajaabdullah.byakugankensaku.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.khawajaabdullah.byakugankensaku.dto.github.PullRequestEvent;
import io.github.khawajaabdullah.byakugankensaku.exception.ByakuganKensakuException;
import io.github.khawajaabdullah.byakugankensaku.service.PullRequestReviewService;
import io.github.khawajaabdullah.byakugankensaku.util.Constant;
import io.github.khawajaabdullah.byakugankensaku.util.GithubProperty;
import io.github.khawajaabdullah.byakugankensaku.util.HmacAlgorithm;
import io.github.khawajaabdullah.byakugankensaku.util.HmacDigestVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/github")
@RestController
public class GithubWebhookController {

  private final GithubProperty githubProperty;
  private final ObjectMapper objectMapper;
  private final PullRequestReviewService pullRequestReviewService;

  public GithubWebhookController(GithubProperty githubProperty, ObjectMapper objectMapper,
                                 PullRequestReviewService pullRequestReviewService) {
    this.githubProperty = githubProperty;
    this.objectMapper = objectMapper;
    this.pullRequestReviewService = pullRequestReviewService;
  }

  @PostMapping("/webhook")
  public void handleWebhook(@RequestHeader("X-Github-Hook-Id") String githubHookId,
                            @RequestHeader("X-GitHub-Event") String githubEvent,
                            @RequestHeader("X-Hub-Signature-256") String hubSignature256,
                            @RequestBody String payload) {
    HmacDigestVerifier.verify(hubSignature256, HmacAlgorithm.HMAC_SHA_256, githubProperty.getWebhookSecret(), payload);
    log.info("Webhook received:- id: {}, event: {}, payload: {}", githubHookId, githubEvent, payload);
    if (Constant.PULL_REQUEST_EVENT.equals(githubEvent)) {
      PullRequestEvent pullRequestEvent;
      try {
        pullRequestEvent = objectMapper.readValue(payload, PullRequestEvent.class);
      } catch (JsonProcessingException e) {
        throw new ByakuganKensakuException("Failed to deserialize pull request event: " + e.getMessage(), e);
      }
      if (pullRequestEvent == null || !Constant.REVIEWABLE_PULL_REQUEST_EVENT_ACTIONS.contains(pullRequestEvent.action())) {
        return;
      }
      pullRequestReviewService.review(pullRequestEvent);
    }
  }

}
