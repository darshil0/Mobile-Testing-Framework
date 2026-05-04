package com.example;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WaitHelper;

public class ExampleTest extends BaseTest {

  @Test(description = "Verify app launches successfully")
  public void testAppLaunch() {
    // Example: Verify an element is displayed after the app loads.
    // Replace with your actual element locator.
    try {
      WebElement element =
          WaitHelper.waitForPresence(driver, AppiumBy.id("com.example.app:id/main_screen"), 20);
      Assert.assertTrue(element.isDisplayed(), "App did not launch successfully");
    } catch (Exception e) {
      Assert.fail("Failed to launch app: " + e.getMessage());
    }
  }

  @Test(description = "Example test with actions")
  public void testBasicInteraction() {
    try {
      // Wait for button to be clickable, then click.
      WebElement button =
          WaitHelper.waitForClickability(driver, AppiumBy.accessibilityId("buttonId"), 15);
      button.click();

      // Verify result element appears.
      WebElement resultText =
          WaitHelper.waitForPresence(
              driver,
              AppiumBy.xpath("//android.widget.TextView[@text='Expected Result']"),
              15);
      Assert.assertTrue(resultText.isDisplayed(), "Expected result not displayed");
    } catch (Exception e) {
      Assert.fail("Test interaction failed: " + e.getMessage());
    }
  }
}
