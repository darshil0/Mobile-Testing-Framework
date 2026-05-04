package listeners;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.DriverManager;

/**
 * TestNG listener for logging test events and taking screenshots on failure. Handles test lifecycle
 * events including start, success, failure, and skip.
 */
public class TestListener implements ITestListener {
  private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

  @Override
  public void onTestStart(ITestResult result) {
    logger.info("========================================");
    logger.info("TEST STARTED: {}", result.getName());
    logger.info("========================================");
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    logger.info("========================================");
    logger.info("TEST PASSED: {}", result.getName());
    logger.info("Duration: {} ms", result.getEndMillis() - result.getStartMillis());
    logger.info("========================================");
  }

  @Override
  public void onTestFailure(ITestResult result) {
    logger.error("========================================");
    logger.error("TEST FAILED: {}", result.getName());
    logger.error("Duration: {} ms", result.getEndMillis() - result.getStartMillis());

    Throwable throwable = result.getThrowable();
    if (throwable != null) {
      logger.error("Error Message: {}", throwable.getMessage());
      logger.error("Stack Trace: ", throwable);
    }

    // Take screenshot on failure
    takeScreenshot(result.getName());

    logger.error("========================================");
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    logger.warn("========================================");
    logger.warn("TEST SKIPPED: {}", result.getName());

    Throwable throwable = result.getThrowable();
    if (throwable != null) {
      logger.warn("Skip Reason: {}", throwable.getMessage());
    }

    logger.warn("========================================");
  }

  @Override
  public void onStart(ITestContext context) {
    logger.info("========================================");
    logger.info("TEST SUITE STARTED: {}", context.getName());
    logger.info("========================================");
  }

  @Override
  public void onFinish(ITestContext context) {
    logger.info("========================================");
    logger.info("TEST SUITE FINISHED: {}", context.getName());
    logger.info("Total Tests: {}", context.getAllTestMethods().length);
    logger.info("Passed: {}", context.getPassedTests().size());
    logger.info("Failed: {}", context.getFailedTests().size());
    logger.info("Skipped: {}", context.getSkippedTests().size());
    logger.info("========================================");
  }

  /**
   * Takes a screenshot and saves it to the reports/screenshots directory. Uses DriverManager to
   * access the current driver instance.
   *
   * @param testName The name of the test to use in the screenshot filename.
   */
  private void takeScreenshot(String testName) {
    try {
      // Check if driver is available
      if (DriverManager.getDriver() == null) {
        logger.warn("Cannot take screenshot - driver is null");
        return;
      }

      TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
      File source = screenshot.getScreenshotAs(OutputType.FILE);

      String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String fileName = testName + "_" + timestamp + ".png";
      String destination = "reports/screenshots/" + fileName;

      File destFile = new File(destination);
      destFile.getParentFile().mkdirs();

      FileUtils.copyFile(source, destFile);
      logger.info("Screenshot saved to: {}", destination);
    } catch (Exception e) {
      logger.error("Failed to take screenshot: {}", e.getMessage());
      logger.debug("Screenshot error details: ", e);
    }
  }
}
