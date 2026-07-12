package io.github.khawajaabdullah.byakugankensaku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class OllamaClient implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(OllamaClient.class);

  private final ChatClient chatClient;

  public OllamaClient(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @Override
  public void run(String... args) throws Exception {
    String response = chatClient.prompt()
        .user("Review this Java code and find issues: int x = null;")
        .call()
        .content();
    LOGGER.info("Beyakugan Kensaku says: {}", response);
  }

}
