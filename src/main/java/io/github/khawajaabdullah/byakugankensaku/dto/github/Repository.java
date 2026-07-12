package io.github.khawajaabdullah.byakugankensaku.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Repository(@JsonProperty("full_name") String fullName) {
}
