package com.github.discordportier.server.model.rest.response;

import java.io.Serializable;

public interface PortierPayload extends Serializable {
  String getPayloadTypeName();
}
