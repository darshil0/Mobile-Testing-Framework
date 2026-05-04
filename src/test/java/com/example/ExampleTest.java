package com.example;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WaitHelper;

public class ExampleTest extends BaseTest {

  @Test(description = "Verify app launches successfully")
  public void testAppLaunch() {
    // Example: Verify an element is displayed
    // Replace with your actual element locator
    try {
      WaitHelper.waitForPresenceOfElement(
          driver, AppiumBy.id("com.example.app:id/main_screen"), 20);
      WebElement element = driver.findElement(AppiumBy.id("com.example.app:id/main_screen"));
      Assert.assertTrue(element.isDisplayed(), "App did not launch successfully");
    } catch (Exception e) {
      Assert.fail("Failed to launch app: " + e.getMessage());
    }
  }

  @Test(description = "Example test with actions")
  public void testBasicInteraction() {
    try {
      // Example: Find and click a button
      WaitHelper.waitForElementToBeClickable(driver, AppiumBy.accessibilityId("buttonId"), 15);
      WebElement button = driver.findElement(AppiumBy.accessibilityId("buttonId"));
      button.click();

      // Verify result
      WaitHelper.waitForPresenceOfElement(
          driver, AppiumBy.xpath("//android.widget.TextView[@text='Expected Result']"), 15);
      WebElement resultText =
          driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Expected Result']"));
      Assert.assertTrue(resultText.isDisplayed(), "Expected result not displayed");
    } catch (Exception e) {
      Assert.fail("Test interaction failed: " + e.getMessage());
    }
  }
}
