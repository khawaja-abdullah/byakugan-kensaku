package io.github.khawajaabdullah.byakugankensaku.util;

import java.util.List;

public final class Constant {

  public static final String HEADER_GITHUB_HOOK_ID = "X-Github-Hook-Id";
  public static final String HEADER_GITHUB_EVENT = "X-GitHub-Event";
  public static final String HEADER_HUB_SIGNATURE_256 = "X-Hub-Signature-256";

  public static final String ASSIGNMENT_OPERATOR = "=";

  public static final String PULL_REQUEST_EVENT = "pull_request";
  public static final String PULL_REQUEST_EVENT_ACTION_OPENED = "opened";
  public static final String PULL_REQUEST_EVENT_ACTION_REOPENED = "reopened";
  public static final String PULL_REQUEST_EVENT_ACTION_SYNCHRONIZE = "synchronize";
  public static final List<String> REVIEWABLE_PULL_REQUEST_EVENT_ACTIONS = List.of(
      PULL_REQUEST_EVENT_ACTION_OPENED,
      PULL_REQUEST_EVENT_ACTION_REOPENED,
      PULL_REQUEST_EVENT_ACTION_SYNCHRONIZE
  );

  public static final String PROMPT = """
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

  private Constant() {
  }

}
