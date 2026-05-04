package com.example;

import io.appium.java_client.AppiumDriver;
import java.net.MalformedURLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import utils.DriverManager;

public class BaseTest {
  protected AppiumDriver driver;

  @BeforeMethod
  @Parameters({"platform"})
  public void setUp(String platform) throws MalformedURLException {
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
