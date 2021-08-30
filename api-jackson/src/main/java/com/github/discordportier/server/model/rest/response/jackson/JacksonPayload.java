package com.github.discordportier.server.model.rest.response.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.github.discordportier.server.model.rest.response.ErrorPayload;
import com.github.discordportier.server.model.rest.response.PongPayload;
import com.github.discordportier.server.model.rest.response.PortierPayload;

@JsonTypeInfo(use = Id.NAME, property = "payload_type")
@JsonSubTypes({
    @Type(name = ErrorPayload.JSON_TYPE_IDENTIFIER, value = ErrorJacksonPayload.class),
    @Type(name = PongPayload.JSON_TYPE_IDENTIFIER, value = PongJacksonPayload.class),
})
public interface JacksonPayload extends PortierPayload {
}
