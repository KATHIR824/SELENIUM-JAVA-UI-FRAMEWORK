package com.company.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for capturing test execution screenshots as files or byte arrays for reporting.
 */
public final class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
        // Prevent instantiation
    }

    /**
     * Captures a screenshot from the active WebDriver and saves it to the target/screenshots directory.
     *
     * @param driver   the active {@link WebDriver}
     * @param testName name of the test or descriptive file prefix
     * @return absolute path string of saved screenshot, or null on failure
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            return null;
        }
        try {
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            Path destPath = Paths.get(System.getProperty("user.dir"), "target", "screenshots", fileName);
            Files.createDirectories(destPath.getParent());
            Files.copy(source.toPath(), destPath);
            return destPath.toString();
        } catch (IOException e) {
            logger.error("Failed to save screenshot for test: {}", testName, e);
            return null;
        }
    }

    /**
     * Captures a screenshot from the active WebDriver as a byte array for Allure reporting attachments.
     *
     * @param driver the active {@link WebDriver}
     * @return byte array containing PNG image bytes
     */
    public static byte[] takeScreenshotAsBytes(WebDriver driver) {
        if (driver == null) {
            return new byte[0];
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as bytes: ", e);
            return new byte[0];
        }
    }
}
