package io.github.khawajaabdullah.byakugankensaku.service;

import io.github.khawajaabdullah.byakugankensaku.dto.domain.FileDiff;
import io.github.khawajaabdullah.byakugankensaku.dto.domain.ReviewComment;
import io.github.khawajaabdullah.byakugankensaku.dto.github.PullRequestEvent;
import io.github.khawajaabdullah.byakugankensaku.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PullRequestReviewService {

  private final GithubService githubService;
  private final AiCodeReviewService aiCodeReviewService;

  @Async
  public void reviewIfApplicable(PullRequestEvent pullRequestEvent) {
    if (!Constant.REVIEWABLE_PULL_REQUEST_EVENT_ACTIONS.contains(pullRequestEvent.action())) return;

    List<FileDiff> fileDiffs = githubService.getDiff(pullRequestEvent);
    fileDiffs.forEach(fileDiff -> {
      List<ReviewComment> reviewComments = aiCodeReviewService.review(fileDiff);
      reviewComments.forEach(reviewComment ->
          githubService.postComment(pullRequestEvent, reviewComment, fileDiff.name())
      );
    });
  }

}
