package com.mobile.testing.tests;

import com.mobile.testing.utils.DriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * A simple Smoke Test to verify the environment and driver initialization. This test does not
 * perform complex app interactions, ensuring that the basic framework setup is functional.
 */
public class SmokeTest extends BaseTest {

  @Test(description = "Verify Appium driver initialization")
  public void testDriverInitialization() {
    try {
      // The driver is initialized in BaseTest.setUp()
      Assert.assertNotNull(DriverManager.getDriver(), "Appium driver should be initialized");
      Assert.assertNotNull(driver, "Driver protected field in BaseTest should be assigned");

      String platform = driver.getCapabilities().getPlatformName().toString();
      System.out.println("Smoke Test running on platform: " + platform);

    } catch (Exception e) {
      Assert.fail("Smoke Test failed during driver initialization: " + e.getMessage());
    }
  }
}
