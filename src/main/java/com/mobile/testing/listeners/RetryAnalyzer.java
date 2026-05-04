package com.mobile.testing.listeners;

import com.mobile.testing.utils.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Implementation of IRetryAnalyzer to automatically retry failed tests. The maximum retry count is
 * read from the configuration file.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
  private int count = 0;
  private static final int MAX_RETRY_COUNT =
      ConfigReader.getInstance().getPlatformIntCapability("testSettings", "retryCount", 0);

  @Override
  public boolean retry(ITestResult result) {
    if (!result.isSuccess()) {
      if (count < MAX_RETRY_COUNT) {
        count++;
        result.setStatus(ITestResult.FAILURE);
        return true;
      }
    } else {
      result.setStatus(ITestResult.SUCCESS);
    }
    return false;
  }
}
