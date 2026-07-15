package io.github.khawajaabdullah.byakugankensaku.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github")
@Data
public class GithubProperty {

  private String apiToken;
  private String webhookSecret;

}
