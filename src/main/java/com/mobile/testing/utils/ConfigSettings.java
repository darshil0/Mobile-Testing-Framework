package com.mobile.testing.utils;

import com.google.gson.JsonElement;
import java.util.Optional;

/** Additional configuration accessors kept separate from the JSON parsing implementation. */
public final class ConfigSettings {
  private ConfigSettings() {}

  public static int retryCount() {
    return ConfigReader.getInstance()
        .getCapabilityValue("testSettings", "retryCount", JsonElement::getAsInt)
        .orElse(0);
  }
}
