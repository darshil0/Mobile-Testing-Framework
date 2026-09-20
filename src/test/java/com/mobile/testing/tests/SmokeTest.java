package com.mobile.testing.tests;

import com.mobile.testing.utils.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Verifies that the framework can create a driver session. */
public class SmokeTest extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(SmokeTest.class);

  @Test(description = "Verify Appium driver initialization")
  public void testDriverInitialization() {
    Assert.assertNotNull(DriverManager.getDriver(), "Appium driver should be initialized");
    Assert.assertNotNull(driver, "BaseTest driver should be assigned");
    logger.info("Smoke test running on platform: {}", driver.getCapabilities().getPlatformName());
  }
}
