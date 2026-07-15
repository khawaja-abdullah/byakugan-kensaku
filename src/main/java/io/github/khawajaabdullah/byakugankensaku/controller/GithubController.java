package io.github.khawajaabdullah.byakugankensaku.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.khawajaabdullah.byakugankensaku.dto.github.PullRequestEvent;
import io.github.khawajaabdullah.byakugankensaku.exception.ByakuganKensakuException;
import io.github.khawajaabdullah.byakugankensaku.service.PullRequestReviewService;
import io.github.khawajaabdullah.byakugankensaku.util.Constant;
import io.github.khawajaabdullah.byakugankensaku.util.GithubProperty;
import io.github.khawajaabdullah.byakugankensaku.util.HmacAlgorithm;
import io.github.khawajaabdullah.byakugankensaku.util.HmacDigestVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/github")
@RestController
public class GithubController {

  private final GithubProperty githubProperty;
  private final ObjectMapper objectMapper;
  private final PullRequestReviewService pullRequestReviewService;

  @PostMapping("/webhook")
  public void handleWebhook(@RequestHeader(Constant.HEADER_GITHUB_HOOK_ID) String githubHookId,
                            @RequestHeader(Constant.HEADER_GITHUB_EVENT) String githubEvent,
                            @RequestHeader(Constant.HEADER_HUB_SIGNATURE_256) String hubSignature256,
                            @RequestBody String payload) {
    HmacDigestVerifier.verify(hubSignature256, HmacAlgorithm.HMAC_SHA_256, githubProperty.getWebhookSecret(), payload);
    log.info("Webhook received:- id: {}, event: {}, payload: {}", githubHookId, githubEvent, payload);
    if (!Constant.PULL_REQUEST_EVENT.equals(githubEvent)) return;
    PullRequestEvent pullRequestEvent = parsePullRequestEvent(payload);
    pullRequestReviewService.reviewIfApplicable(pullRequestEvent);
  }

  private PullRequestEvent parsePullRequestEvent(String payload) {
    try {
      return objectMapper.readValue(payload, PullRequestEvent.class);
    } catch (JsonProcessingException e) {
      throw new ByakuganKensakuException("Failed to deserialize pull request event: " + e.getMessage(), e);
    }
  }

}
