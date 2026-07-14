package io.github.khawajaabdullah.byakugankensaku.service;

import io.github.khawajaabdullah.byakugankensaku.dto.domain.FileDiff;
import io.github.khawajaabdullah.byakugankensaku.dto.domain.ReviewComment;
import io.github.khawajaabdullah.byakugankensaku.dto.github.PullRequestEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PullRequestReviewService {

  private final GithubService githubService;
  private final AiCodeReviewService aiCodeReviewService;

  public PullRequestReviewService(GithubService githubService, AiCodeReviewService aiCodeReviewService) {
    this.githubService = githubService;
    this.aiCodeReviewService = aiCodeReviewService;
  }

  @Async
  public void review(PullRequestEvent pullRequestEvent) {
    List<FileDiff> fileDiffs = githubService.getDiff(pullRequestEvent);
    fileDiffs.forEach(fileDiff -> {
      List<ReviewComment> reviewComments = aiCodeReviewService.review(fileDiff);
      reviewComments.forEach(reviewComment -> githubService.postComment(pullRequestEvent, reviewComment, fileDiff.name()));
    });
  }

}
