package io.github.khawajaabdullah.byakugankensaku.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.khawajaabdullah.byakugankensaku.dto.domain.FileDiff;
import io.github.khawajaabdullah.byakugankensaku.dto.domain.ReviewComment;
import io.github.khawajaabdullah.byakugankensaku.exception.ByakuganKensakuException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OllamaCodeReviewService {

  private static final String PROMPT = """
      You are an expert Java code reviewer. Review the following git diff and identify:
      - Bugs or logic errors
      - Security vulnerabilities
      - Performance issues (N+1 queries, unnecessary loops)
      - Missing null checks
      - Poor naming or readability issues
      
      For each issue found, respond in this exact JSON format:
      [
        {
          "line": <line number in the diff>,
          "comment": "<your review comment>"
        }
      ]
      
      If no issues found, respond with an empty array: []
      Only respond with JSON. No explanation outside the JSON.
      
      Diff to review:
      %s
      """;

  private final ChatClient chatClient;
  private final ObjectMapper objectMapper;

  public OllamaCodeReviewService(ChatClient chatClient, ObjectMapper objectMapper) {
    this.chatClient = chatClient;
    this.objectMapper = objectMapper;
  }

  public List<ReviewComment> review(FileDiff fileDiff) {
    String formattedPrompt = PROMPT.formatted(fileDiff.patch());
    log.info("Sending prompt to code reviewer:- \n{}", formattedPrompt);
    String response = chatClient.prompt()
        .user(formattedPrompt)
        .call()
        .content();
    log.info("Code reviewer responded:- \n{}", response);
    if (StringUtils.isBlank(response)) {
      return Collections.emptyList();
    }
    String sanitizedResponse = response.replace("```json", "").replace("```", "").trim();
    try {
      return objectMapper.readValue(sanitizedResponse, new TypeReference<List<ReviewComment>>() {
      });
    } catch (JsonProcessingException e) {
      throw new ByakuganKensakuException("Failed to deserialize review comments from response: " + e.getMessage(), e);
    }
  }

}
