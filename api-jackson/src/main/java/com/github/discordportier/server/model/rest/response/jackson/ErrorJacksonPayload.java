package com.github.discordportier.server.model.rest.response.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discordportier.server.model.rest.response.ErrorCode;
import com.github.discordportier.server.model.rest.response.ErrorPayload;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true) // The impl may be newer than classpath's API.
public class ErrorJacksonPayload extends ErrorPayload implements JacksonPayload {
  // Only used for Jackson deserialization.
  @JsonCreator(mode = Mode.PROPERTIES)
  /* package-private */ ErrorJacksonPayload(
      @JsonProperty(JSON_FIELD_CODE) final String code,
      @JsonProperty(JSON_FIELD_DETAIL) final String detail
  ) {
    super(code, detail);
  }

  public ErrorJacksonPayload(final ErrorCode errorCode, final String detail) {
    this(errorCode.name(), detail);
  }

  @Override
  @JsonGetter(JSON_FIELD_CODE)
  public String getCode() {
    return super.getCode();
  }

  @Override
  @JsonGetter(JSON_FIELD_DETAIL)
  public String getDetail() {
    return super.getDetail();
  }

  @Override
  @JsonIgnore
  public Optional<ErrorCode> getCodeAsEnum() {
    return super.getCodeAsEnum();
  }

  @Override
  @JsonIgnore
  public String getPayloadTypeName() {
    return super.getPayloadTypeName();
  }
}
