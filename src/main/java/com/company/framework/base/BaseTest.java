package com.company.framework.base;

import com.company.framework.config.ConfigReader;
import com.company.framework.driver.DriverFactory;
import com.company.framework.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base TestNG class managing WebDriver initialization and teardown across test executions.
 */
public abstract class BaseTest {

    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Initializes the WebDriver, configures timeouts and window state, and navigates to the base application URL.
     *
     * @param browser optional browser parameter from TestNG suite XML
     */
    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional String browser) {
        String targetBrowser = (browser != null && !browser.trim().isEmpty()) ? browser.trim() : ConfigReader.getBrowser();
        logger.info("Setting up WebDriver instance for browser: {}", targetBrowser);

        WebDriver driver = DriverFactory.createDriver(targetBrowser);
        DriverManager.setDriver(driver);

        if (!ConfigReader.isHeadless()) {
            DriverManager.getDriver().manage().window().maximize();
        }

        String baseUrl = ConfigReader.getBaseUrl();
        logger.info("Navigating to Base URL: {}", baseUrl);
        DriverManager.getDriver().get(baseUrl);
    }

    /**
     * Quits the active WebDriver and cleans up ThreadLocal references.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                logger.info("Tearing down WebDriver instance");
                driver.quit();
            }
        } catch (Exception e) {
            logger.error("Error encountered while quitting WebDriver: ", e);
        } finally {
            DriverManager.unload();
        }
    }
}
