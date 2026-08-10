package io.github.khawajaabdullah.byakugankensaku.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.github.khawajaabdullah.byakugankensaku.util.Constant.JSON_PROPERTY_PULL_REQUEST;

public record PullRequestEvent(String action, int number, @JsonProperty(JSON_PROPERTY_PULL_REQUEST) PullRequest pullRequest,
                               Repository repository) {
}
