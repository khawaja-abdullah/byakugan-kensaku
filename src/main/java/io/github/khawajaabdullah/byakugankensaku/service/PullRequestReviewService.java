package io.github.khawajaabdullah.byakugankensaku.service;

import io.github.khawajaabdullah.byakugankensaku.dto.FileDiff;
import io.github.khawajaabdullah.byakugankensaku.dto.github.PullRequestEvent;
import io.github.khawajaabdullah.byakugankensaku.dto.ReviewComment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PullRequestReviewService {

  private final GithubService githubService;
  private final OllamaCodeReviewService ollamaCodeReviewService;

  public PullRequestReviewService(GithubService githubService, OllamaCodeReviewService ollamaCodeReviewService) {
    this.githubService = githubService;
    this.ollamaCodeReviewService = ollamaCodeReviewService;
  }

  @Async
  public void review(PullRequestEvent pullRequestEvent) {
    List<FileDiff> fileDiffs = githubService.getDiff(pullRequestEvent);
    fileDiffs.forEach(fileDiff -> {
      List<ReviewComment> reviewComments = ollamaCodeReviewService.review(fileDiff);
      reviewComments.forEach(reviewComment -> githubService.postComment(pullRequestEvent, reviewComment, fileDiff.name()));
    });
  }

}
