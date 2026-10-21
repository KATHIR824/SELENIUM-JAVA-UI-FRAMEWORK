package com.company.framework.listeners;

import com.company.framework.driver.DriverManager;
import com.company.framework.utils.ScreenshotUtils;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG test listener for logging test execution lifecycles and capturing Allure attachments on test failure.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("================================================================================");
        logger.info("TEST STARTED: {}", result.getMethod().getMethodName());
        logger.info("================================================================================");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("TEST PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("TEST FAILED: {}", testName);
        if (result.getThrowable() != null) {
            logger.error("Failure Exception: ", result.getThrowable());
        }

        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            byte[] screenshot = ScreenshotUtils.takeScreenshotAsBytes(driver);
            if (screenshot.length > 0) {
                saveScreenshot(testName, screenshot);
            }
            saveTextLog("Test failure captured on method: " + testName + "\nException: " +
                    (result.getThrowable() != null ? result.getThrowable().getMessage() : "N/A"));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("TEST SKIPPED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting Test Suite execution: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finished Test Suite execution: {}", context.getName());
    }

    /**
     * Attaches screenshot bytes directly to the Allure report.
     *
     * @param name       the screenshot name
     * @param screenshot byte array of PNG image
     * @return screenshot byte array
     */
    @Attachment(value = "Page Screenshot - {0}", type = "image/png")
    public byte[] saveScreenshot(String name, byte[] screenshot) {
        return screenshot;
    }

    /**
     * Attaches plain text logs to the Allure report.
     *
     * @param message log message
     * @return message string
     */
    @Attachment(value = "Execution Log", type = "text/plain")
    public String saveTextLog(String message) {
        return message;
    }
}
