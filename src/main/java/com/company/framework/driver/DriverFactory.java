package com.company.framework.driver;

import com.company.framework.config.ConfigReader;
import com.company.framework.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class responsible for initializing {@link WebDriver} instances
 * for supported browsers (Chrome, Firefox, Edge) using Selenium Manager.
 */
public final class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
        // Prevent instantiation
    }

    /**
     * Creates and configures a new {@link WebDriver} instance based on framework configuration.
     *
     * @return initialized {@link WebDriver}
     */
    public static WebDriver createDriver() {
        String browser = ConfigReader.getBrowser();
        return createDriver(browser);
    }

    /**
     * Creates and configures a new {@link WebDriver} instance for the specified browser name.
     *
     * @param browser the target browser name ("chrome", "firefox", "edge")
     * @return initialized {@link WebDriver}
     */
    public static WebDriver createDriver(String browser) {
        if (browser == null || browser.trim().isEmpty()) {
            browser = ConfigReader.getBrowser();
        }
        browser = browser.trim().toLowerCase();
        boolean headless = ConfigReader.isHeadless();

        logger.info("Initializing WebDriver for browser: {} | Headless: {}", browser, headless);

        WebDriver driver;
        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.default_content_setting_values.notifications", 2);
                chromeOptions.setExperimentalOption("prefs", prefs);
                chromeOptions.addArguments("--disable-features=PasswordLeakDetection");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--remote-allow-origins=*");

                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                }
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--window-size=1920,1080");
                    edgeOptions.addArguments("--disable-gpu");
                } else {
                    edgeOptions.addArguments("--start-maximized");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new FrameworkException("Unsupported browser specified: '" + browser + "'. Supported: chrome, firefox, edge");
        }

        return driver;
    }
}
