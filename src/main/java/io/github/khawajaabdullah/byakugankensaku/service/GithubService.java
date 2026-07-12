package io.github.khawajaabdullah.byakugankensaku.service;

import io.github.khawajaabdullah.byakugankensaku.dto.domain.FileDiff;
import io.github.khawajaabdullah.byakugankensaku.dto.github.PullRequestEvent;
import io.github.khawajaabdullah.byakugankensaku.dto.domain.ReviewComment;
import io.github.khawajaabdullah.byakugankensaku.exception.ByakuganKensakuException;
import io.micrometer.common.util.StringUtils;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GithubService {

  private final GitHub github;

  public GithubService(GitHub github) {
    this.github = github;
  }

  public List<FileDiff> getDiff(PullRequestEvent pullRequestEvent) {
    try {
      GHRepository ghRepository = github.getRepository(pullRequestEvent.repository().fullName());
      GHPullRequest ghPullRequest = ghRepository.getPullRequest(pullRequestEvent.number());
      return ghPullRequest.listFiles()
          .toList()
          .stream()
          .filter(file -> !StringUtils.isBlank(file.getPatch()))
          .map(file -> new FileDiff(file.getFilename(), file.getPatch(), file.getSha()))
          .toList();
    } catch (IOException e) {
      throw new ByakuganKensakuException("Failed to fetch pull request diff: " + e.getMessage(), e);
    }
  }

  public void postComment(PullRequestEvent pullRequestEvent, ReviewComment reviewComment, String fileName) {
    try {
      GHRepository ghRepository = github.getRepository(pullRequestEvent.repository().fullName());
      GHPullRequest ghPullRequest = ghRepository.getPullRequest(pullRequestEvent.number());
      ghPullRequest.createReviewComment()
          .body(reviewComment.comment())
          .commitId(pullRequestEvent.pullRequest().head().sha())
          .path(fileName)
          .line(reviewComment.line())
          .create();
    } catch (IOException e) {
      throw new ByakuganKensakuException("Failed to post comment: " + e.getMessage(), e);
    }
  }

}
