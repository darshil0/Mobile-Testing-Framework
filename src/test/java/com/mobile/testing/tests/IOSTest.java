package com.mobile.testing.tests;

import com.mobile.testing.utils.WaitHelper;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Example test class for iOS platform. Demonstrates the use of iOS-specific locators and
 * cross-platform accessibility IDs.
 */
public class IOSTest extends BaseTest {

  @Test(description = "Verify iOS app launches and shows main screen")
  public void testAppLaunch() {
    try {
      // iOS often uses Accessibility IDs or Class Chains
      // Replace with your actual iOS locator
      WebElement element =
          WaitHelper.waitForVisibility(driver, AppiumBy.accessibilityId("main_screen_title"), 20);
      Assert.assertTrue(element.isDisplayed(), "iOS App did not launch to main screen");
    } catch (Exception e) {
      Assert.fail("iOS App launch failed: " + e.getMessage());
    }
  }

  @Test(description = "Verify basic interaction on iOS")
  public void testIOSTapsAndInputs() {
    try {
      // Accessibility IDs are the preferred cross-platform locator
      WebElement loginButton =
          WaitHelper.waitForClickability(driver, AppiumBy.accessibilityId("login_button"), 15);
      loginButton.click();

      // Example of iOS Predicate String
      WebElement errorMsg =
          WaitHelper.waitForPresence(
              driver, AppiumBy.iOSNsPredicateString("label == 'Invalid Credentials'"), 10);
      Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed on iOS");
    } catch (Exception e) {
      Assert.fail("iOS interaction test failed: " + e.getMessage());
    }
  }
}
