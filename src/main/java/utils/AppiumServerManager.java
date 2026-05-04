package utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to programmatically start and stop the Appium server.
 */
public class AppiumServerManager {
  private static final Logger logger = LoggerFactory.getLogger(AppiumServerManager.class);
  private static AppiumDriverLocalService service;

  /** Starts the Appium server using default configuration. */
  public static void startServer() {
    if (service == null || !service.isRunning()) {
      logger.info("Starting Appium server...");
      AppiumServiceBuilder builder = new AppiumServiceBuilder();
      builder.withIPAddress("127.0.0.1");
      builder.usingAnyFreePort();
      builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
      builder.withArgument(GeneralServerFlag.LOG_LEVEL, "info");

      service = AppiumDriverLocalService.buildService(builder);
      service.start();
      logger.info("Appium server started on: {}", service.getUrl());
    }
  }

  /** Stops the Appium server if it is running. */
  public static void stopServer() {
    if (service != null && service.isRunning()) {
      logger.info("Stopping Appium server...");
      service.stop();
      logger.info("Appium server stopped.");
    }
  }

  /**
   * Gets the URL of the running Appium server.
   *
   * @return The server URL, or null if not running.
   */
  public static String getServerUrl() {
    return (service != null && service.isRunning()) ? service.getUrl().toString() : null;
  }
}
