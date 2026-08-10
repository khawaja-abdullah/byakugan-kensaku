package io.github.khawajaabdullah.byakugankensaku.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.github.khawajaabdullah.byakugankensaku.util.Constant.JSON_PROPERTY_FULL_NAME;

public record Repository(@JsonProperty(JSON_PROPERTY_FULL_NAME) String fullName) {
}
