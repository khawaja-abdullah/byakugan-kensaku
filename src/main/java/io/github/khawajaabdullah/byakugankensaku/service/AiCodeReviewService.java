package io.github.khawajaabdullah.byakugankensaku.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.khawajaabdullah.byakugankensaku.dto.domain.FileDiff;
import io.github.khawajaabdullah.byakugankensaku.dto.domain.ReviewComment;
import io.github.khawajaabdullah.byakugankensaku.exception.ByakuganKensakuException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static io.github.khawajaabdullah.byakugankensaku.util.Constant.PROMPT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiCodeReviewService {

  private final ChatClient chatClient;
  private final ObjectMapper objectMapper;

  public List<ReviewComment> review(FileDiff fileDiff) {
    String formattedPrompt = PROMPT.formatted(fileDiff.patch());
    log.info("Sending prompt to AI code reviewer:- \n{}", formattedPrompt);

    String response = chatClient.prompt()
        .user(formattedPrompt)
        .call()
        .content();
    log.info("AI Code reviewer responded:- \n{}", response);

    if (StringUtils.isBlank(response)) {
      return Collections.emptyList();
    }

    String sanitizedResponse = response.replace("```json", StringUtils.EMPTY)
        .replace("```", StringUtils.EMPTY)
        .trim();

    return parseReviewComments(sanitizedResponse);
  }

  private List<ReviewComment> parseReviewComments(String response) {
    try {
      return objectMapper.readValue(response, new TypeReference<List<ReviewComment>>() {
      });
    } catch (JsonProcessingException e) {
      throw new ByakuganKensakuException("Failed to parse review comments from response: " + e.getMessage(), e);
    }
  }

}
