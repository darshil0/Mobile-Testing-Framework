package utils;

import io.appium.java_client.AppiumDriver;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for basic visual regression testing.
 * Compares current screen state against a baseline image.
 */
public class VisualRegressionHelper {
  private static final Logger logger = LoggerFactory.getLogger(VisualRegressionHelper.class);
  private static final String BASELINE_DIR = "src/test/resources/baselines/";
  private static final String DIFF_DIR = "reports/visual-diffs/";

  /**
   * Compares the current screen against a baseline image.
   *
   * @param driver The AppiumDriver.
   * @param baselineName The name of the baseline image (without extension).
   * @param tolerance The allowed percentage of differing pixels (0.0 to 100.0).
   * @return true if the current screen matches the baseline within the tolerance.
   */
  public static boolean verifyScreen(AppiumDriver driver, String baselineName, double tolerance) {
    try {
      File baselineFile = new File(BASELINE_DIR + baselineName + ".png");
      
      TakesScreenshot ts = (TakesScreenshot) driver;
      File actualFile = ts.getScreenshotAs(OutputType.FILE);
      BufferedImage actualImage = ImageIO.read(actualFile);

      if (!baselineFile.exists()) {
        logger.warn("Baseline image not found. Saving current screen as baseline: {}", baselineFile.getPath());
        baselineFile.getParentFile().mkdirs();
        ImageIO.write(actualImage, "png", baselineFile);
        return true; // Auto-approve the first run
      }

      BufferedImage baselineImage = ImageIO.read(baselineFile);

      if (baselineImage.getWidth() != actualImage.getWidth() || baselineImage.getHeight() != actualImage.getHeight()) {
        logger.error("Image dimensions mismatch. Baseline: {}x{}, Actual: {}x{}", 
            baselineImage.getWidth(), baselineImage.getHeight(), actualImage.getWidth(), actualImage.getHeight());
        return false;
      }

      int diffPixels = 0;
      int totalPixels = baselineImage.getWidth() * baselineImage.getHeight();
      BufferedImage diffImage = new BufferedImage(baselineImage.getWidth(), baselineImage.getHeight(), BufferedImage.TYPE_INT_RGB);

      for (int y = 0; y < baselineImage.getHeight(); y++) {
        for (int x = 0; x < baselineImage.getWidth(); x++) {
          int rgbBaseline = baselineImage.getRGB(x, y);
          int rgbActual = actualImage.getRGB(x, y);

          if (rgbBaseline != rgbActual) {
            diffPixels++;
            diffImage.setRGB(x, y, Color.RED.getRGB()); // Highlight differences in red
          } else {
            diffImage.setRGB(x, y, rgbActual); // Keep original color where they match
          }
        }
      }

      double diffPercentage = ((double) diffPixels / totalPixels) * 100.0;
      logger.info("Visual comparison for '{}': {}% difference", baselineName, String.format("%.2f", diffPercentage));

      if (diffPercentage > tolerance) {
        File diffFile = new File(DIFF_DIR + baselineName + "_diff.png");
        diffFile.getParentFile().mkdirs();
        ImageIO.write(diffImage, "png", diffFile);
        logger.error("Visual mismatch exceeds tolerance! Diff saved to: {}", diffFile.getPath());
        return false;
      }

      return true;
    } catch (Exception e) {
      logger.error("Error during visual regression testing: {}", e.getMessage(), e);
      return false;
    }
  }
}
