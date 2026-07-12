package io.github.khawajaabdullah.byakugankensaku.exception;

public class ByakuganKensakuException extends RuntimeException {

  public ByakuganKensakuException(String message) {
    super(message);
  }

  public ByakuganKensakuException(String message, Throwable cause) {
    super(message, cause);
  }

}
