package com.github.discordportier.server.model.rest.response.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discordportier.server.model.rest.response.PongPayload;

@JsonIgnoreProperties(ignoreUnknown = true) // The impl may be newer than classpath's API.
public class PongJacksonPayload extends PongPayload implements JacksonPayload {
  @JsonCreator(mode = Mode.PROPERTIES)
  public PongJacksonPayload(
      @JsonProperty(JSON_FIELD_TIMESTAMP) final long timestamp
  ) {
    super(timestamp);
  }

  @Override
  @JsonGetter(JSON_FIELD_TIMESTAMP)
  public long getTimestamp() {
    return super.getTimestamp();
  }

  @Override
  @JsonIgnore
  public String getPayloadTypeName() {
    return super.getPayloadTypeName();
  }
}
