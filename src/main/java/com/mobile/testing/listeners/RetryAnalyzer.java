package com.mobile.testing.listeners;

import com.mobile.testing.utils.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** Retries a failed test up to the configured number of times. */
public class RetryAnalyzer implements IRetryAnalyzer {
  private int attempts;

  @Override
  public boolean retry(ITestResult result) {
    int maxRetries = ConfigReader.getInstance().getRetryCount();
    if (result.isSuccess() || attempts >= maxRetries) {
      return false;
    }
    attempts++;
    return true;
  }
}
