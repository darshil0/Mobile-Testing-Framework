package utils;

import io.appium.java_client.AppiumDriver;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Utility class for handling explicit waits. */
public class WaitHelper {
  private static final Logger logger = LoggerFactory.getLogger(WaitHelper.class);

  /**
   * Gets the default explicit wait timeout from the configuration.
   *
   * @return The default timeout in seconds.
   */
  private static int getDefaultTimeout() {
    return ConfigReader.getInstance().getExplicitWait();
  }

  /**
   * Waits for an element to be visible using a custom timeout.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @param timeoutInSeconds The custom timeout in seconds.
   */
  public static void waitForElementToBeVisible(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      wait.until(ExpectedConditions.visibilityOf(element));
    } catch (Exception e) {
      logger.error("Element not visible within {} seconds", timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for an element to be visible using the default timeout.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   */
  public static void waitForElementToBeVisible(AppiumDriver driver, WebElement element) {
    waitForElementToBeVisible(driver, element, getDefaultTimeout());
  }

  /**
   * Waits for an element located by a locator to be visible.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @param timeoutInSeconds The custom timeout in seconds.
   */
  public static void waitForElementToBeVisible(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    } catch (Exception e) {
      logger.error(
          "Element with locator {} not visible within {} seconds", locator, timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for an element located by a locator to be visible using the default timeout.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   */
  public static void waitForElementToBeVisible(AppiumDriver driver, By locator) {
    waitForElementToBeVisible(driver, locator, getDefaultTimeout());
  }

  /**
   * Waits for an element to be clickable using a custom timeout.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @param timeoutInSeconds The custom timeout in seconds.
   */
  public static void waitForElementToBeClickable(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      wait.until(ExpectedConditions.elementToBeClickable(element));
    } catch (Exception e) {
      logger.error("Element not clickable within {} seconds", timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for an element to be clickable using the default timeout.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   */
  public static void waitForElementToBeClickable(AppiumDriver driver, WebElement element) {
    waitForElementToBeClickable(driver, element, getDefaultTimeout());
  }

  /**
   * Waits for an element located by a locator to be clickable using a custom timeout.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @param timeoutInSeconds The custom timeout in seconds.
   */
  public static void waitForElementToBeClickable(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      wait.until(ExpectedConditions.elementToBeClickable(locator));
    } catch (Exception e) {
      logger.error(
          "Element with locator {} not clickable within {} seconds", locator, timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for an element located by a locator to be clickable using the default timeout.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   */
  public static void waitForElementToBeClickable(AppiumDriver driver, By locator) {
    waitForElementToBeClickable(driver, locator, getDefaultTimeout());
  }

  /**
   * Waits for an element to be invisible using a custom timeout.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @param timeoutInSeconds The custom timeout in seconds.
   */
  public static void waitForElementToBeInvisible(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      wait.until(ExpectedConditions.invisibilityOf(element));
    } catch (Exception e) {
      logger.error("Element still visible after {} seconds", timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for an element to be invisible using the default timeout.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   */
  public static void waitForElementToBeInvisible(AppiumDriver driver, WebElement element) {
    waitForElementToBeInvisible(driver, element, getDefaultTimeout());
  }

  /**
   * Waits for the presence of an element using a custom timeout.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @param timeoutInSeconds The custom timeout in seconds.
   */
  public static void waitForPresenceOfElement(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    } catch (Exception e) {
      logger.error(
          "Element with locator {} not present within {} seconds", locator, timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for the presence of an element using the default timeout.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   */
  public static void waitForPresenceOfElement(AppiumDriver driver, By locator) {
    waitForPresenceOfElement(driver, locator, getDefaultTimeout());
  }

  /**
   * Waits for an element to disappear from the DOM.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @param timeoutInSeconds The custom timeout in seconds.
   * @return True if the element disappears, false otherwise.
   */
  public static boolean waitForElementToDisappear(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    } catch (Exception e) {
      logger.warn(
          "Element with locator {} still present after {} seconds", locator, timeoutInSeconds);
      return false;
    }
  }

  /**
   * A custom wait that uses a specific ExpectedCondition.
   *
   * @param driver The Appium driver.
   * @param timeoutInSeconds The custom timeout in seconds.
   * @param condition The ExpectedCondition to wait for.
   */
  public static <T> void customWait(
      AppiumDriver driver, int timeoutInSeconds, ExpectedCondition<T> condition) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      wait.until(condition);
    } catch (Exception e) {
      logger.error("Custom wait condition not met within {} seconds", timeoutInSeconds, e);
      throw e;
    }
  }
}
