package io.github.khawajaabdullah.byakugankensaku.util;

import lombok.Getter;
import org.apache.commons.codec.digest.HmacAlgorithms;

@Getter
public enum HmacAlgorithm {

  HMAC_SHA_256(HmacAlgorithms.HMAC_SHA_256, "sha256"),
  HMAC_SHA_1(HmacAlgorithms.HMAC_SHA_1, "sha1"),
  HMAC_SHA_512(HmacAlgorithms.HMAC_SHA_512, "sha512"),
  HMAC_MD5(HmacAlgorithms.HMAC_MD5, "md5");

  private final HmacAlgorithms name;
  private final String shortName;

  HmacAlgorithm(HmacAlgorithms name, String shortName) {
    this.name = name;
    this.shortName = shortName;
  }

}
