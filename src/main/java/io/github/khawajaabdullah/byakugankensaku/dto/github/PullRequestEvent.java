package io.github.khawajaabdullah.byakugankensaku.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PullRequestEvent(String action, int number, @JsonProperty("pull_request") PullRequest pullRequest,
                               Repository repository) {
}
