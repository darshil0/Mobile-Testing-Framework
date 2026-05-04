package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for triggering deep links on both Android and iOS platforms.
 */
public class DeepLinkHelper {
  private static final Logger logger = LoggerFactory.getLogger(DeepLinkHelper.class);

  /**
   * Opens a deep link URL in the connected application.
   *
   * @param driver The AppiumDriver instance.
   * @param url The deep link URL to open (e.g., "myapp://home").
   * @param appPackage The package name for Android, or bundle ID for iOS.
   */
  public static void openDeepLink(AppiumDriver driver, String url, String appPackage) {
    logger.info("Opening deep link: {} for package/bundle: {}", url, appPackage);

    if (driver instanceof AndroidDriver) {
      // Android specific deep link trigger via adb shell
      Map<String, String> args = Map.of(
          "url", url,
          "package", appPackage
      );
      driver.executeScript("mobile: deepLink", args);
    } else if (driver instanceof IOSDriver) {
      // iOS specific deep link trigger via Safari or direct executeScript
      // Note: In modern Appium, the 'mobile: deepLink' is also supported for iOS
      Map<String, String> args = Map.of(
          "url", url,
          "bundleId", appPackage
      );
      try {
        driver.executeScript("mobile: deepLink", args);
      } catch (Exception e) {
        logger.warn("mobile: deepLink failed, falling back to traditional iOS URL opening: {}", e.getMessage());
        driver.get(url);
      }
    } else {
      logger.error("Unsupported driver type for deep linking: {}", driver.getClass().getName());
      throw new UnsupportedOperationException("Deep links are only supported on AndroidDriver and IOSDriver");
    }
  }
}
