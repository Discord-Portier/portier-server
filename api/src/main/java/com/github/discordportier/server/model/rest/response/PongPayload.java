package com.github.discordportier.server.model.rest.response;

public class PongPayload implements PortierPayload {
  public static final String JSON_TYPE_IDENTIFIER = "PONG";
  public static final String JSON_FIELD_TIMESTAMP = "timestamp";

  private final long timestamp;

  public PongPayload(final long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String getPayloadTypeName() {
    return JSON_TYPE_IDENTIFIER;
  }

  public long getTimestamp() {
    return this.timestamp;
  }
}
