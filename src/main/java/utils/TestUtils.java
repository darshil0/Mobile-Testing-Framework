package utils;

import io.appium.java_client.AppiumDriver;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * General-purpose test utility methods: screenshot capture, safe element interactions, and
 * scroll helpers. All methods are stateless and accept the driver explicitly so they remain safe
 * for use in multi-threaded / parallel execution contexts.
 */
public class TestUtils {
  private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);
  private static final String SCREENSHOT_DIR = "reports/screenshots/";

  private TestUtils() {
    // Utility class — do not instantiate.
  }

  /**
   * Captures a screenshot and saves it to {@code reports/screenshots/}.
   *
   * <p>The filename format is {@code <name>_yyyyMMdd_HHmmss.png}.
   *
   * @param driver The Appium driver.
   * @param name A descriptive label used in the filename (e.g. {@code "checkout_page"}).
   */
  public static void takeScreenshot(AppiumDriver driver, String name) {
    try {
      TakesScreenshot ts = (TakesScreenshot) driver;
      File source = ts.getScreenshotAs(OutputType.FILE);

      String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String fileName = name + "_" + timestamp + ".png";
      File destination = new File(SCREENSHOT_DIR + fileName);
      destination.getParentFile().mkdirs();

      FileUtils.copyFile(source, destination);
      logger.info("Screenshot saved: {}", destination.getPath());
    } catch (Exception e) {
      logger.error("Failed to take screenshot '{}': {}", name, e.getMessage(), e);
    }
  }

  /**
   * Waits for {@code element} to be clickable and then clicks it.
   *
   * <p>Uses the default explicit-wait timeout from {@code config.json}.
   *
   * @param driver The Appium driver.
   * @param element The element to click.
   */
  public static void clickElement(AppiumDriver driver, WebElement element) {
    try {
      WebElement clickable = WaitHelper.waitForClickability(driver, element);
      clickable.click();
      logger.debug("Clicked element: {}", element);
    } catch (Exception e) {
      logger.error("Failed to click element: {}", e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Clears the element's current value and types the supplied text.
   *
   * @param element The target input element.
   * @param text The text to enter.
   */
  public static void sendKeys(WebElement element, String text) {
    try {
      element.clear();
      element.sendKeys(text);
      logger.debug("Sent keys '{}' to element: {}", text, element);
    } catch (Exception e) {
      logger.error("Failed to send keys to element: {}", e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Waits for {@code element} to be visible within the given timeout.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @param timeoutInSeconds Maximum time to wait, in seconds.
   * @return The visible element.
   */
  public static WebElement waitForElement(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    return WaitHelper.waitForVisibility(driver, element, timeoutInSeconds);
  }

  /**
   * Scrolls the screen in the specified {@code direction} until {@code element} is visible.
   *
   * @param driver The Appium driver.
   * @param element The element to scroll to.
   * @param direction The direction to swipe in while searching (e.g. {@code Direction.UP}).
   */
  public static void scrollToElement(
      AppiumDriver driver, WebElement element, GestureHelper.Direction direction) {
    GestureHelper.scrollToElement(driver, element, direction);
  }

  /**
   * Scrolls the screen until {@code element} is visible by delegating to {@link
   * GestureHelper#scrollToElement(AppiumDriver, WebElement)}. Default direction is {@code UP}.
   *
   * @param driver The Appium driver.
   * @param element The element to scroll to.
   */
  public static void scrollToElement(AppiumDriver driver, WebElement element) {
    GestureHelper.scrollToElement(driver, element);
  }

  /**
   * Navigates to a deep link in the application.
   *
   * @param driver The AppiumDriver.
   * @param url The deep link URL.
   */
  public static void openDeepLink(AppiumDriver driver, String url) {
    ConfigReader config = ConfigReader.getInstance();
    String platformName = driver.getCapabilities().getPlatformName().toString();
    String appPackage = "android".equalsIgnoreCase(platformName)
        ? config.getPlatformCapability("android", "appPackage")
        : config.getPlatformCapability("ios", "bundleId");

    DeepLinkHelper.openDeepLink(driver, url, appPackage);
  }
}
