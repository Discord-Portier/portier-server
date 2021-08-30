package com.github.discordportier.server.model.rest.response;

import java.util.Optional;

public class ErrorPayload implements PortierPayload {
  public static final String JSON_TYPE_IDENTIFIER = "ERROR";
  public static final String JSON_FIELD_CODE = "code";
  public static final String JSON_FIELD_DETAIL = "detail";

  private final String code;
  private final String detail;

  public ErrorPayload(final String code, final String detail) {
    this.code = code;
    this.detail = detail;
  }

  @Override
  public String getPayloadTypeName() {
    return JSON_TYPE_IDENTIFIER;
  }

  public Optional<ErrorCode> getCodeAsEnum() {
    return ErrorCode.findIfPresent(this.code);
  }

  public String getCode() {
    return this.code;
  }

  public String getDetail() {
    return this.detail;
  }
}
