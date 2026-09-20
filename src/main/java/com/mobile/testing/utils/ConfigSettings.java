package com.mobile.testing.utils;

/** Additional configuration accessors kept separate from the JSON parsing implementation. */
public final class ConfigSettings {
  private ConfigSettings() {}

  public static int retryCount() {
    return ConfigReader.getInstance().getRetryCount();
  }
}
