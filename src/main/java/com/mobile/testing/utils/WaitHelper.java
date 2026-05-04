package com.mobile.testing.utils;

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

  // -------------------------------------------------------------------------
  // Visibility waits
  // -------------------------------------------------------------------------

  /**
   * Waits for a WebElement to be visible and returns it.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @param timeoutInSeconds Custom timeout in seconds.
   * @return The visible element.
   */
  public static WebElement waitForVisibility(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      return wait.until(ExpectedConditions.visibilityOf(element));
    } catch (Exception e) {
      logger.error("Element not visible within {} seconds", timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for a WebElement to be visible using the default timeout and returns it.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @return The visible element.
   */
  public static WebElement waitForVisibility(AppiumDriver driver, WebElement element) {
    return waitForVisibility(driver, element, getDefaultTimeout());
  }

  /**
   * Waits for an element located by a locator to be visible and returns it.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @param timeoutInSeconds Custom timeout in seconds.
   * @return The visible element.
   */
  public static WebElement waitForVisibility(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    } catch (Exception e) {
      logger.error(
          "Element with locator {} not visible within {} seconds", locator, timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for an element located by a locator to be visible using the default timeout and returns
   * it.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @return The visible element.
   */
  public static WebElement waitForVisibility(AppiumDriver driver, By locator) {
    return waitForVisibility(driver, locator, getDefaultTimeout());
  }

  // -------------------------------------------------------------------------
  // Clickability waits
  // -------------------------------------------------------------------------

  /**
   * Waits for a WebElement to be clickable and returns it.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @param timeoutInSeconds Custom timeout in seconds.
   * @return The clickable element.
   */
  public static WebElement waitForClickability(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      return wait.until(ExpectedConditions.elementToBeClickable(element));
    } catch (Exception e) {
      logger.error("Element not clickable within {} seconds", timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for a WebElement to be clickable using the default timeout and returns it.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @return The clickable element.
   */
  public static WebElement waitForClickability(AppiumDriver driver, WebElement element) {
    return waitForClickability(driver, element, getDefaultTimeout());
  }

  /**
   * Waits for an element located by a locator to be clickable and returns it.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @param timeoutInSeconds Custom timeout in seconds.
   * @return The clickable element.
   */
  public static WebElement waitForClickability(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      return wait.until(ExpectedConditions.elementToBeClickable(locator));
    } catch (Exception e) {
      logger.error(
          "Element with locator {} not clickable within {} seconds", locator, timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for an element located by a locator to be clickable using the default timeout and returns
   * it.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @return The clickable element.
   */
  public static WebElement waitForClickability(AppiumDriver driver, By locator) {
    return waitForClickability(driver, locator, getDefaultTimeout());
  }

  // -------------------------------------------------------------------------
  // Presence waits
  // -------------------------------------------------------------------------

  /**
   * Waits for an element located by a locator to be present in the DOM and returns it.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @param timeoutInSeconds Custom timeout in seconds.
   * @return The present element.
   */
  public static WebElement waitForPresence(AppiumDriver driver, By locator, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    } catch (Exception e) {
      logger.error(
          "Element with locator {} not present within {} seconds", locator, timeoutInSeconds, e);
      throw e;
    }
  }

  /**
   * Waits for an element located by a locator to be present in the DOM using the default timeout
   * and returns it.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @return The present element.
   */
  public static WebElement waitForPresence(AppiumDriver driver, By locator) {
    return waitForPresence(driver, locator, getDefaultTimeout());
  }

  // -------------------------------------------------------------------------
  // Legacy void API — kept for backward compatibility
  // -------------------------------------------------------------------------

  /**
   * @deprecated Use {@link #waitForVisibility(AppiumDriver, WebElement, int)} instead.
   */
  @Deprecated
  public static void waitForElementToBeVisible(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    waitForVisibility(driver, element, timeoutInSeconds);
  }

  /**
   * @deprecated Use {@link #waitForVisibility(AppiumDriver, WebElement)} instead.
   */
  @Deprecated
  public static void waitForElementToBeVisible(AppiumDriver driver, WebElement element) {
    waitForVisibility(driver, element);
  }

  /**
   * @deprecated Use {@link #waitForVisibility(AppiumDriver, By, int)} instead.
   */
  @Deprecated
  public static void waitForElementToBeVisible(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    waitForVisibility(driver, locator, timeoutInSeconds);
  }

  /**
   * @deprecated Use {@link #waitForVisibility(AppiumDriver, By)} instead.
   */
  @Deprecated
  public static void waitForElementToBeVisible(AppiumDriver driver, By locator) {
    waitForVisibility(driver, locator);
  }

  /**
   * @deprecated Use {@link #waitForClickability(AppiumDriver, WebElement, int)} instead.
   */
  @Deprecated
  public static void waitForElementToBeClickable(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    waitForClickability(driver, element, timeoutInSeconds);
  }

  /**
   * @deprecated Use {@link #waitForClickability(AppiumDriver, WebElement)} instead.
   */
  @Deprecated
  public static void waitForElementToBeClickable(AppiumDriver driver, WebElement element) {
    waitForClickability(driver, element);
  }

  /**
   * @deprecated Use {@link #waitForClickability(AppiumDriver, By, int)} instead.
   */
  @Deprecated
  public static void waitForElementToBeClickable(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    waitForClickability(driver, locator, timeoutInSeconds);
  }

  /**
   * @deprecated Use {@link #waitForClickability(AppiumDriver, By)} instead.
   */
  @Deprecated
  public static void waitForElementToBeClickable(AppiumDriver driver, By locator) {
    waitForClickability(driver, locator);
  }

  /**
   * @deprecated Use {@link #waitForPresence(AppiumDriver, By, int)} instead.
   */
  @Deprecated
  public static void waitForPresenceOfElement(
      AppiumDriver driver, By locator, int timeoutInSeconds) {
    waitForPresence(driver, locator, timeoutInSeconds);
  }

  /**
   * @deprecated Use {@link #waitForPresence(AppiumDriver, By)} instead.
   */
  @Deprecated
  public static void waitForPresenceOfElement(AppiumDriver driver, By locator) {
    waitForPresence(driver, locator);
  }

  // -------------------------------------------------------------------------
  // Invisibility waits
  // -------------------------------------------------------------------------

  /**
   * Waits for an element to be invisible.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @param timeoutInSeconds Custom timeout in seconds.
   * @return {@code true} if the element is invisible, {@code false} otherwise.
   */
  public static boolean waitForElementToBeInvisible(
      AppiumDriver driver, WebElement element, int timeoutInSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      return wait.until(ExpectedConditions.invisibilityOf(element));
    } catch (Exception e) {
      logger.error("Element still visible after {} seconds", timeoutInSeconds, e);
      return false;
    }
  }

  /**
   * Waits for an element to be invisible using the default timeout.
   *
   * @param driver The Appium driver.
   * @param element The element to wait for.
   * @return {@code true} if the element is invisible, {@code false} otherwise.
   */
  public static boolean waitForElementToBeInvisible(AppiumDriver driver, WebElement element) {
    return waitForElementToBeInvisible(driver, element, getDefaultTimeout());
  }

  /**
   * Waits for an element to disappear from the DOM.
   *
   * @param driver The Appium driver.
   * @param locator The locator of the element.
   * @param timeoutInSeconds Custom timeout in seconds.
   * @return {@code true} if the element disappears within the timeout, {@code false} otherwise.
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

  // -------------------------------------------------------------------------
  // Custom condition wait
  // -------------------------------------------------------------------------

  /**
   * Waits for a custom {@link ExpectedCondition} to be met.
   *
   * @param driver The Appium driver.
   * @param timeoutInSeconds Custom timeout in seconds.
   * @param condition The condition to wait for.
   * @param <T> The return type of the condition.
   * @return The result of the condition.
   */
  public static <T> T customWait(
      AppiumDriver driver, int timeoutInSeconds, ExpectedCondition<T> condition) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
      return wait.until(condition);
    } catch (Exception e) {
      logger.error("Custom wait condition not met within {} seconds", timeoutInSeconds, e);
      throw e;
    }
  }
}
