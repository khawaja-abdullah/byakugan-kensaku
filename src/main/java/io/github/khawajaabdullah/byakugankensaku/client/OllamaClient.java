package io.github.khawajaabdullah.byakugankensaku.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(OllamaClient.class);

  private final ChatClient chatClient;

  public OllamaClient(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public String query(String input) {
    String response = chatClient.prompt()
        .user(input)
        .call()
        .content();
    LOGGER.info("Beyakugan Kensaku says: {}", response);
    return response;
  }

}
