package io.github.khawajaabdullah.byakugankensaku;

import io.github.khawajaabdullah.byakugankensaku.util.GithubProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableConfigurationProperties(GithubProperty.class)
@SpringBootApplication
public class ByakuganKensakuApplication {

  public static void main(String[] args) {
    SpringApplication.run(ByakuganKensakuApplication.class, args);
  }

}
