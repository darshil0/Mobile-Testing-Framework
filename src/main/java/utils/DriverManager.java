package utils;

import exceptions.DriverException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.options.BaseOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the Appium driver instance. This class is responsible for initializing and quitting the
 * driver.
 */
public class DriverManager {
  private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
  private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
  private static final String ANDROID = "android";
  private static final String IOS = "ios";

  /**
   * Initializes the Appium driver for the specified platform.
   *
   * @param platform The platform to initialize the driver for ("android" or "ios").
   * @throws MalformedURLException If the Appium URL is invalid.
   */
  public static void initializeDriver(String platform) throws MalformedURLException {
    if (driver.get() != null) {
      logger.warn("Driver already initialized. Quitting existing driver.");
      quitDriver();
    }

    ConfigReader config = ConfigReader.getInstance();
    URL appiumUrl = new URL(config.getAppiumUrl());

    logger.info("Initializing driver for platform: {}", platform);

    if (ANDROID.equalsIgnoreCase(platform)) {
      UiAutomator2Options options = new UiAutomator2Options();
      setCommonCapabilities(platform, options);
      options.setAppPackage(config.getPlatformCapability(ANDROID, "appPackage"));
      options.setAppActivity(config.getPlatformCapability(ANDROID, "appActivity"));
      options.setAutoGrantPermissions(
          config.getPlatformBooleanCapability(ANDROID, "autoGrantPermissions", true));

      logger.info("Creating AndroidDriver with URL: {}", appiumUrl);
      driver.set(new AndroidDriver(appiumUrl, options));

    } else if (IOS.equalsIgnoreCase(platform)) {
      XCUITestOptions options = new XCUITestOptions();
      setCommonCapabilities(platform, options);
      options.setBundleId(config.getPlatformCapability(IOS, "bundleId"));
      options.setAutoAcceptAlerts(
          config.getPlatformBooleanCapability(IOS, "autoAcceptAlerts", true));

      logger.info("Creating IOSDriver with URL: {}", appiumUrl);
      driver.set(new IOSDriver(appiumUrl, options));

    } else {
      throw new IllegalArgumentException(
          "Invalid platform: " + platform + ". Must be 'android' or 'ios'.");
    }

    setupImplicitWait();
  }

  /**
   * Sets common capabilities for both Android and iOS.
   *
   * @param platform The target platform.
   * @param options The options object to configure.
   */
  private static void setCommonCapabilities(String platform, BaseOptions<?> options) {
    ConfigReader config = ConfigReader.getInstance();

    options.setPlatformName(config.getPlatformCapability(platform, "platformName"));
    options.setPlatformVersion(config.getPlatformCapability(platform, "platformVersion"));
    options.setAutomationName(config.getPlatformCapability(platform, "automationName"));

    options.setCapability("appium:deviceName", config.getPlatformCapability(platform, "deviceName"));
    options.setCapability("appium:app", config.getPlatformCapability(platform, "app"));
    options.setCapability("appium:noReset", config.isNoReset());
    options.setCapability("appium:fullReset", config.isFullReset());

    int timeout = config.getPlatformIntCapability(platform, "newCommandTimeout", 300);
    options.setCapability("appium:newCommandTimeout", timeout);
  }

  /** Sets the implicit wait for the current driver instance. */
  private static void setupImplicitWait() {
    AppiumDriver currentDriver = getDriver();
    if (currentDriver != null) {
      int implicitWait = ConfigReader.getInstance().getImplicitWait();
      currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
      logger.info(
          "Driver initialized successfully with implicit wait of {} seconds.", implicitWait);
    }
  }

  /**
   * Gets the current Appium driver instance.
   *
   * <p>Returns {@code null} if the driver has not been initialized or has already been quit.
   * Callers that require a live driver should use {@link #requireDriver()} instead.
   *
   * @return The Appium driver, or {@code null} if not initialized.
   */
  public static AppiumDriver getDriver() {
    return driver.get();
  }

  /**
   * Gets the current Appium driver instance, throwing if it has not been initialized.
   *
   * @return The Appium driver.
   * @throws DriverException If the driver has not been initialized.
   */
  public static AppiumDriver requireDriver() {
    AppiumDriver currentDriver = driver.get();
    if (currentDriver == null) {
      throw new DriverException("Driver not initialized. Call initializeDriver() first.");
    }
    return currentDriver;
  }

  /** Quits the Appium driver and removes it from the ThreadLocal storage. */
  public static void quitDriver() {
    if (driver.get() != null) {
      try {
        logger.info("Quitting driver.");
        driver.get().quit();
        logger.info("Driver quit successfully.");
      } catch (Exception e) {
        logger.error("Error while quitting driver", e);
      } finally {
        driver.remove();
      }
    }
  }
}
