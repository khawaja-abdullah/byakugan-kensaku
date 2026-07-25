package io.github.khawajaabdullah.byakugankensaku.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {

  @Bean
  @ConditionalOnClass(name = "org.springframework.ai.ollama.OllamaChatModel")
  public ChatClient chatClient(OllamaChatModel ollamaChatModel) {
    return ChatClient.builder(ollamaChatModel).build();
  }

}
