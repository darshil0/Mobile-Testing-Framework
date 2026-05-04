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

public class BaseTest {
  protected AppiumDriver driver;

  @BeforeSuite
  public void beforeSuite() {
    AppiumServerManager.startServer();
  }

  @AfterSuite(alwaysRun = true)
  public void afterSuite() {
    AppiumServerManager.stopServer();
  }

  @BeforeMethod
  @Parameters({"platform"})
  public void setUp(@Optional("android") String platform) throws MalformedURLException {
    if (platform == null || platform.isEmpty()) {
      platform = "android";
    }
    DriverManager.initializeDriver(platform);
    driver = DriverManager.getDriver();
  }

  @AfterMethod
  public void tearDown() {
    DriverManager.quitDriver();
  }

  public AppiumDriver getDriver() {
    return driver;
  }
}
