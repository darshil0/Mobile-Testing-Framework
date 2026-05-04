package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles reading and parsing the configuration file (config.json). This class follows the
 * Singleton pattern to ensure only one instance is created.
 */
public class ConfigReader {
  private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
  private static volatile ConfigReader instance;
  private JsonObject config;

  // Configuration file keys
  private static final String CONFIG_FILE_PATH = Paths.get("config", "config.json").toString();
  private static final String ANDROID = "android";
  private static final String IOS = "ios";
  private static final String APPIUM = "appiumServer";
  private static final String TEST_SETTINGS = "testSettings";
  private static final String HOST = "host";
  private static final String PORT = "port";
  private static final String IMPLICIT_WAIT = "implicitWait";
  private static final String EXPLICIT_WAIT = "explicitWait";
  private static final String SCREENSHOT_ON_FAILURE = "screenshotOnFailure";
  private static final String NO_RESET = "noReset";

  /**
   * Private constructor to prevent instantiation from outside. Loads the config.json file and
   * parses it into a JsonObject.
   */
  private ConfigReader() {
    try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
      config = JsonParser.parseReader(reader).getAsJsonObject();
      logger.info("Configuration loaded successfully from {}", CONFIG_FILE_PATH);
    } catch (IOException e) {
      logger.error("Failed to load configuration file: {}", CONFIG_FILE_PATH, e);
      throw new RuntimeException("Failed to load config.json", e);
    }
  }

  /**
   * Returns the singleton instance of the ConfigReader.
   *
   * @return The singleton instance.
   */
  public static ConfigReader getInstance() {
    if (instance == null) {
      synchronized (ConfigReader.class) {
        if (instance == null) {
          instance = new ConfigReader();
        }
      }
    }
    return instance;
  }

  /**
   * Gets a capability value for a given platform.
   *
   * @param platform The platform ("android" or "ios").
   * @param key The capability key.
   * @return The capability value as a String, or null if not found.
   */
  public String getPlatformCapability(String platform, String key) {
    return getCapability(platform, key, JsonElement::getAsString).orElse(null);
  }

  /**
   * Gets a boolean capability value for a given platform.
   *
   * @param platform The platform ("android" or "ios").
   * @param key The capability key.
   * @param defaultValue The default value to return if the key is not found.
   * @return The capability value as a boolean.
   */
  public boolean getPlatformBooleanCapability(String platform, String key, boolean defaultValue) {
    return getCapability(platform, key, JsonElement::getAsBoolean).orElse(defaultValue);
  }

  /**
   * Gets an integer capability value for a given platform.
   *
   * @param platform The platform ("android" or "ios").
   * @param key The capability key.
   * @param defaultValue The default value to return if the key is not found.
   * @return The capability value as an integer.
   */
  public int getPlatformIntCapability(String platform, String key, int defaultValue) {
    return getCapability(platform, key, JsonElement::getAsInt).orElse(defaultValue);
  }

  /**
   * Generic private method to retrieve and convert a capability.
   *
   * @param platform The platform section in the JSON.
   * @param key The key of the capability.
   * @param converter A function to convert the JsonElement to the target type.
   * @param <T> The target type.
   * @return An Optional containing the value, or empty if not found or on error.
   */
  private <T> Optional<T> getCapability(
      String platform, String key, Function<JsonElement, T> converter) {
    try {
      JsonObject platformConfig = config.getAsJsonObject(platform);
      if (platformConfig == null) {
        return Optional.empty();
      }
      JsonElement element = platformConfig.get(key);
      if (element == null || element.isJsonNull()) {
        return Optional.empty();
      }

      if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
        String resolvedValue = resolveEnvironmentVariable(element.getAsString());
        JsonElement newElement;
        try {
          // Try to parse as JSON for booleans, numbers, etc.
          newElement = JsonParser.parseString(resolvedValue);
        } catch (com.google.gson.JsonSyntaxException e) {
          // If parsing fails, treat it as a literal string
          newElement = new com.google.gson.JsonPrimitive(resolvedValue);
        }
        return Optional.of(converter.apply(newElement));
      }
      return Optional.of(converter.apply(element));
    } catch (Exception e) {
      logger.warn("Failed to get capability: {}.{}. Using default.", platform, key);
      return Optional.empty();
    }
  }

  /**
   * Resolves environment variables in the config value. Supports the format
   * ${ENV_VAR:-default_value} and can handle multiple placeholders in a single string.
   *
   * @param value The value to resolve.
   * @return The resolved value.
   */
  private String resolveEnvironmentVariable(String value) {
    if (value == null) {
      return null;
    }

    Pattern pattern = Pattern.compile("\\$\\{([^:}]+)(?::-([^}]*))?\\}");
    Matcher matcher = pattern.matcher(value);
    StringBuilder sb = new StringBuilder();

    while (matcher.find()) {
      String envVar = matcher.group(1);
      String defaultValue = matcher.group(2);

      String envValue = System.getenv(envVar);
      if (envValue == null) {
        envValue = System.getProperty(envVar, defaultValue);
      }

      matcher.appendReplacement(
          sb, envValue != null ? Matcher.quoteReplacement(envValue) : matcher.group(0));
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  /**
   * Gets the Appium server URL from the config.
   *
   * @return The Appium server URL.
   */
  public String getAppiumUrl() {
    JsonObject appiumConfig = config.getAsJsonObject(APPIUM);
    if (appiumConfig == null) {
      throw new RuntimeException("Appium server configuration not found in config.json");
    }

    JsonElement hostElement = appiumConfig.get(HOST);
    JsonElement portElement = appiumConfig.get(PORT);

    if (hostElement == null || portElement == null) {
      throw new RuntimeException("Appium host or port missing in config.json");
    }

    String host = resolveEnvironmentVariable(hostElement.getAsString());
    String port = resolveEnvironmentVariable(portElement.getAsString());
    return String.format("http://%s:%s", host, port);
  }

  /**
   * Gets the implicit wait timeout from test settings.
   *
   * @return The implicit wait timeout in seconds.
   */
  public int getImplicitWait() {
    return getCapability(TEST_SETTINGS, IMPLICIT_WAIT, JsonElement::getAsInt).orElse(10);
  }

  /**
   * Gets the explicit wait timeout from test settings.
   *
   * @return The explicit wait timeout in seconds.
   */
  public int getExplicitWait() {
    return getCapability(TEST_SETTINGS, EXPLICIT_WAIT, JsonElement::getAsInt).orElse(30);
  }

  /**
   * Checks if screenshots should be taken on failure.
   *
   * @return True if screenshots are enabled, false otherwise.
   */
  public boolean isScreenshotOnFailure() {
    return getCapability(TEST_SETTINGS, SCREENSHOT_ON_FAILURE, JsonElement::getAsBoolean)
        .orElse(true);
  }

  /**
   * Returns the {@code noReset} flag from the {@code testSettings} section of {@code config.json}.
   *
   * <p>When {@code true}, Appium will not reset the app state between sessions, which avoids a
   * full reinstall and speeds up test execution. Defaults to {@code false} if the key is absent.
   *
   * @return {@code true} if the app should not be reset between sessions.
   */
  public boolean isNoReset() {
    return getCapability(TEST_SETTINGS, NO_RESET, JsonElement::getAsBoolean).orElse(false);
  }
}
