package utils;

import io.appium.java_client.AppiumDriver;
import java.time.Duration;
import java.util.Collections;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Utility class for performing touch gestures using the W3C Actions API. */
public class GestureHelper {
  private static final Logger logger = LoggerFactory.getLogger(GestureHelper.class);
  private static final Duration SWIPE_DURATION = Duration.ofMillis(1000);

  /**
   * Swipes up on the screen (from 80% to 20% of screen height).
   *
   * @param driver The Appium driver.
   */
  public static void swipeUp(AppiumDriver driver) {
    Dimension size = driver.manage().window().getSize();
    int startX = size.width / 2;
    int startY = (int) (size.height * 0.8);
    int endY = (int) (size.height * 0.2);
    swipe(driver, new Point(startX, startY), new Point(startX, endY), SWIPE_DURATION);
  }

  /**
   * Swipes down on the screen (from 20% to 80% of screen height).
   *
   * @param driver The Appium driver.
   */
  public static void swipeDown(AppiumDriver driver) {
    Dimension size = driver.manage().window().getSize();
    int startX = size.width / 2;
    int startY = (int) (size.height * 0.2);
    int endY = (int) (size.height * 0.8);
    swipe(driver, new Point(startX, startY), new Point(startX, endY), SWIPE_DURATION);
  }

  /**
   * Swipes left on the screen (from 80% to 20% of screen width).
   *
   * @param driver The Appium driver.
   */
  public static void swipeLeft(AppiumDriver driver) {
    Dimension size = driver.manage().window().getSize();
    int startX = (int) (size.width * 0.8);
    int endX = (int) (size.width * 0.2);
    int y = size.height / 2;
    swipe(driver, new Point(startX, y), new Point(endX, y), SWIPE_DURATION);
  }

  /**
   * Swipes right on the screen (from 20% to 80% of screen width).
   *
   * @param driver The Appium driver.
   */
  public static void swipeRight(AppiumDriver driver) {
    Dimension size = driver.manage().window().getSize();
    int startX = (int) (size.width * 0.2);
    int endX = (int) (size.width * 0.8);
    int y = size.height / 2;
    swipe(driver, new Point(startX, y), new Point(endX, y), SWIPE_DURATION);
  }

  /**
   * Performs a swipe gesture from a start point to an end point.
   *
   * @param driver The Appium driver.
   * @param start The starting point of the swipe.
   * @param end The ending point of the swipe.
   * @param duration The duration of the swipe.
   */
  public static void swipe(AppiumDriver driver, Point start, Point end, Duration duration) {
    try {
      logger.info("Swiping from {} to {} in {}ms", start, end, duration.toMillis());
      PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
      Sequence swipe = new Sequence(finger, 1);
      swipe.addAction(
          finger.createPointerMove(
              Duration.ZERO, PointerInput.Origin.viewport(), start.x, start.y));
      swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
      swipe.addAction(
          finger.createPointerMove(duration, PointerInput.Origin.viewport(), end.x, end.y));
      swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
      driver.perform(Collections.singletonList(swipe));
    } catch (Exception e) {
      logger.error("Failed to perform swipe", e);
      throw e;
    }
  }

  /**
   * Taps on the center of an element.
   *
   * @param driver The Appium driver.
   * @param element The element to tap.
   */
  public static void tap(AppiumDriver driver, WebElement element) {
    Point center = getCenter(element);
    try {
      logger.info("Tapping on element at center: {}", center);
      PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
      Sequence tap = new Sequence(finger, 1);
      tap.addAction(
          finger.createPointerMove(
              Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y));
      tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
      tap.addAction(new Pause(finger, Duration.ofMillis(100)));
      tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
      driver.perform(Collections.singletonList(tap));
    } catch (Exception e) {
      logger.error("Failed to tap element", e);
      throw e;
    }
  }

  /**
   * Long presses on the center of an element for the given number of seconds. Convenience overload
   * of {@link #longPress(AppiumDriver, WebElement, Duration)}.
   *
   * @param driver The Appium driver.
   * @param element The element to long press.
   * @param seconds The duration of the long press in seconds.
   */
  public static void longPress(AppiumDriver driver, WebElement element, int seconds) {
    longPress(driver, element, Duration.ofSeconds(seconds));
  }

  /**
   * Long presses on the center of an element for the given duration.
   *
   * @param driver The Appium driver.
   * @param element The element to long press.
   * @param duration The duration of the long press.
   */
  public static void longPress(AppiumDriver driver, WebElement element, Duration duration) {
    Point center = getCenter(element);
    try {
      logger.info(
          "Long pressing element at center {} for {} seconds", center, duration.getSeconds());
      PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
      Sequence longPress = new Sequence(finger, 1);
      longPress.addAction(
          finger.createPointerMove(
              Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y));
      longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
      longPress.addAction(new Pause(finger, duration));
      longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
      driver.perform(Collections.singletonList(longPress));
    } catch (Exception e) {
      logger.error("Failed to long press element", e);
      throw e;
    }
  }

  /**
   * Scrolls to an element by swiping up until the element is visible.
   *
   * @param driver The Appium driver.
   * @param element The element to scroll to.
   */
  public static void scrollToElement(AppiumDriver driver, WebElement element) {
    try {
      logger.info("Scrolling to element");
      int maxScrolls = 10;
      int scrollCount = 0;

      while (!isElementInView(element) && scrollCount < maxScrolls) {
        swipeUp(driver);
        scrollCount++;
      }

      if (scrollCount == maxScrolls) {
        logger.warn("Element not found after {} scrolls", maxScrolls);
      }
    } catch (Exception e) {
      logger.error("Failed to scroll to element", e);
      throw e;
    }
  }

  private static Point getCenter(WebElement element) {
    Point location = element.getLocation();
    Dimension size = element.getSize();
    return new Point(location.x + size.width / 2, location.y + size.height / 2);
  }

  private static boolean isElementInView(WebElement element) {
    try {
      return element.isDisplayed();
    } catch (Exception e) {
      return false;
    }
  }
}
