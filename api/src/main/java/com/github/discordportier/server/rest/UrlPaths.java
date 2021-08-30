package com.github.discordportier.server.rest;

/**
 * The valid URL paths for the application.
 */
public enum UrlPaths {
  PING("/ping"),
  ;

  private final String path;

  UrlPaths(final String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  @Override
  public String toString() {
    return "UrlPaths(path = " + this.getPath() + ")";
  }
}
