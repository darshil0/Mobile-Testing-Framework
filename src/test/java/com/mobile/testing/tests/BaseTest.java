package com.mobile.testing.tests;

import com.mobile.testing.utils.AppiumServerManager;
import com.mobile.testing.utils.DriverManager;
import io.appium.java_client.AppiumDriver;
import java.net.MalformedURLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/** Common Appium lifecycle for platform-specific tests. */
public class BaseTest {
  protected AppiumDriver driver;

  @BeforeSuite(alwaysRun = true)
  public void beforeSuite() {
    AppiumServerManager.startServer();
  }

  @AfterSuite(alwaysRun = true)
  public void afterSuite() {
    AppiumServerManager.stopServer();
  }

  @BeforeMethod(alwaysRun = true)
  @Parameters("platform")
  public void setUp(@Optional("android") String platform) throws MalformedURLException {
    String selectedPlatform = platform == null || platform.trim().isEmpty() ? "android" : platform;
    DriverManager.initializeDriver(selectedPlatform.trim());
    driver = DriverManager.requireDriver();
  }

  @AfterMethod(alwaysRun = true)
  public void tearDown() {
    DriverManager.quitDriver();
    driver = null;
  }

  public AppiumDriver getDriver() {
    return driver;
  }
}
