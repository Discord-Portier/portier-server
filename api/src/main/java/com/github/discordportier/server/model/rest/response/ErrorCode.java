package com.github.discordportier.server.model.rest.response;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ErrorCode {
  INTERNAL_SERVER_ERROR,
  ;

  private static final Map<String, ErrorCode> NAME_TO_INSTANCE_INDEX = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(ErrorCode::name, Function.identity()));

  /**
   * Find the matching error code if it is present in the API's enum.
   * <p>
   * This may return {@link Optional#empty() an empty optional} if your API is not the same version
   * as the server's.
   *
   * @param code the error code name
   * @return the matching error code, if any
   */
  public static Optional<ErrorCode> findIfPresent(final String code) {
    return Optional.ofNullable(NAME_TO_INSTANCE_INDEX.get(code));
  }
}
