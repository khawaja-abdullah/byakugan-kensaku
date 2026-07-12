package io.github.khawajaabdullah.byakugankensaku.config;

import io.github.khawajaabdullah.byakugankensaku.util.GithubProperty;
import io.github.khawajaabdullah.byakugankensaku.exception.ByakuganKensakuException;
import io.micrometer.common.util.StringUtils;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GithubClientConfiguration {

  @Bean
  public GitHub github(GithubProperty githubProperty) {
    String githubApiToken = githubProperty.getApiToken();
    if (StringUtils.isBlank(githubApiToken)) {
      throw new ByakuganKensakuException("GitHub API token is not configured. Set 'github.api.token' in application properties.");
    }
    try {
      return new GitHubBuilder()
          .withOAuthToken(githubApiToken)
          .build();
    } catch (IOException e) {
      throw new ByakuganKensakuException("Failed to initialize GitHub service: " + e.getMessage(), e);
    }
  }

}
