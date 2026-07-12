package io.github.khawajaabdullah.byakugankensaku.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "github")
public class GithubProperty {

  private String apiToken;
  private String webhookSecret;

}
