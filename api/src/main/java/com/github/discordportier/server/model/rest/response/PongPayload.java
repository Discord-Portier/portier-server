package com.github.discordportier.server.model.rest.response;

import java.time.ZonedDateTime;

public class PongPayload implements PortierPayload {
  private static final long serialVersionUID = 8323851641130476612L;
  public static final String JSON_TYPE_IDENTIFIER = "PONG";
  public static final String JSON_FIELD_TIMESTAMP = "timestamp";

  private final ZonedDateTime timestamp;

  public PongPayload(final ZonedDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String getPayloadTypeName() {
    return JSON_TYPE_IDENTIFIER;
  }

  public ZonedDateTime getTimestamp() {
    return this.timestamp;
  }
}
