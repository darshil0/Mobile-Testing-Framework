package utils;

import io.appium.java_client.AppiumDriver;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtils {

  public static void takeScreenshot(AppiumDriver driver, String fileName) {
    try {
      File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      File destination = new File("screenshots/" + fileName + "_" + timestamp + ".png");
      destination.getParentFile().mkdirs();
      FileUtils.copyFile(screenshot, destination);
      System.out.println("Screenshot saved: " + destination.getAbsolutePath());
    } catch (Exception e) {
      System.err.println("Failed to take screenshot: " + e.getMessage());
    }
  }

  public static void waitForElement(AppiumDriver driver, WebElement element, int timeoutSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    wait.until(ExpectedConditions.visibilityOf(element));
  }

  public static void clickElement(AppiumDriver driver, WebElement element) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.elementToBeClickable(element));
    element.click();
  }

  public static void sendKeys(WebElement element, String text) {
    element.clear();
    element.sendKeys(text);
  }

  public static void scrollToElement(AppiumDriver driver, WebElement element) {
    GestureHelper.scrollToElement(driver, element);
  }
}
