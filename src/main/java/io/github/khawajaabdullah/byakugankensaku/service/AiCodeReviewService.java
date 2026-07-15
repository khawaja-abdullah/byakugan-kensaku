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

@Service
@RequiredArgsConstructor
@Slf4j
public class AiCodeReviewService {

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
