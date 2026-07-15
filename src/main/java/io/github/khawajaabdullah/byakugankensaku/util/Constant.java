package io.github.khawajaabdullah.byakugankensaku.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class Constant {

  public static final String HEADER_GITHUB_HOOK_ID = "X-Github-Hook-Id";
  public static final String HEADER_GITHUB_EVENT = "X-GitHub-Event";
  public static final String HEADER_HUB_SIGNATURE_256 = "X-Hub-Signature-256";

  public static final Charset UTF_8 = StandardCharsets.UTF_8;
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

  private Constant() {
  }

}
