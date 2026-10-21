package com.company.framework.config;

import com.company.framework.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * Utility class for reading framework configuration properties.
 * <p>
 * Configuration values are resolved with the following priority:
 * <ol>
 *   <li>Java System Properties (e.g., passed via {@code -Dbrowser=firefox -Dheadless=true})</li>
 *   <li>Operating System Environment Variables</li>
 *   <li>Environment-specific properties file (e.g., {@code config/qa.properties} or {@code config/config.properties})</li>
 *   <li>Default fallback values</li>
 * </ol>
 */
public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();
    private static final String DEFAULT_ENV = "config";

    static {
        loadProperties();
    }

    private ConfigReader() {
        // Prevent instantiation
    }

    /**
     * Loads properties from the classpath based on the resolved environment.
     */
    private static void loadProperties() {
        String env = System.getProperty("env");
        if (env == null || env.trim().isEmpty()) {
            env = System.getenv("env");
            if (env == null || env.trim().isEmpty()) {
                env = DEFAULT_ENV;
            }
        }

        String propertiesFilePath = "config/" + env.toLowerCase() + ".properties";
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(propertiesFilePath)) {
            if (inputStream == null) {
                // Fallback to default config.properties if specific env file is missing
                try (InputStream fallbackStream = ConfigReader.class.getClassLoader().getResourceAsStream("config/config.properties")) {
                    if (fallbackStream != null) {
                        properties.load(fallbackStream);
                        logger.info("Loaded fallback configuration from config/config.properties");
                    } else {
                        logger.warn("No properties file found at '{}' or 'config/config.properties'. Using built-in defaults.", propertiesFilePath);
                    }
                }
            } else {
                properties.load(inputStream);
                logger.info("Loaded framework configuration from: {}", propertiesFilePath);
            }
        } catch (Exception e) {
            throw new FrameworkException("Failed to load properties file: " + propertiesFilePath, e);
        }
    }

    /**
     * Resolves a property key by checking System properties first, then Environment variables,
     * and finally the loaded configuration properties.
     *
     * @param key the property key name
     * @return the resolved property string value or null if not found
     */
    public static String getProperty(String key) {
        if (key == null) {
            return null;
        }

        // 1. Check System Properties (-Dkey=value)
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp.trim();
        }

        // 2. Check Environment Variables (exclude Windows USERNAME to prevent collision with app username)
        if (!key.equalsIgnoreCase("username")) {
            String envProp = System.getenv(key);
            if (envProp != null && !envProp.trim().isEmpty()) {
                return envProp.trim();
            }
        }

        // 3. Check loaded properties file
        return properties.getProperty(key);
    }

    /**
     * Gets the target browser name (e.g. chrome, firefox, edge).
     *
     * @return the browser name in lower case, defaults to "chrome"
     */
    public static String getBrowser() {
        String browser = getProperty("browser");
        return (browser != null && !browser.trim().isEmpty()) ? browser.trim().toLowerCase() : "chrome";
    }

    /**
     * Checks if headless mode is enabled.
     *
     * @return true if headless execution is enabled, false otherwise
     */
    public static boolean isHeadless() {
        String headless = getProperty("headless");
        return headless != null && Boolean.parseBoolean(headless.trim());
    }

    /**
     * Gets the base URL for the application under test.
     *
     * @return the base URL string
     */
    public static String getBaseUrl() {
        String url = getProperty("baseUrl");
        return (url != null && !url.trim().isEmpty()) ? url.trim() : "https://www.saucedemo.com";
    }

    /**
     * Gets the default login username.
     *
     * @return username string
     */
    public static String getUsername() {
        String user = getProperty("username");
        return (user != null && !user.trim().isEmpty()) ? user.trim() : "standard_user";
    }

    /**
     * Gets the default login password.
     *
     * @return password string
     */
    public static String getPassword() {
        String pass = getProperty("password");
        return (pass != null && !pass.trim().isEmpty()) ? pass.trim() : "secret_sauce";
    }

    /**
     * Gets the explicit wait timeout in seconds.
     *
     * @return timeout duration in seconds, defaults to 10 seconds
     */
    public static int getTimeout() {
        String timeout = getProperty("timeout");
        try {
            return (timeout != null && !timeout.trim().isEmpty()) ? Integer.parseInt(timeout.trim()) : 10;
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    /**
     * Gets the explicit wait timeout as a {@link Duration}.
     *
     * @return {@link Duration} instance for Selenium explicit waits
     */
    public static Duration getTimeoutDuration() {
        return Duration.ofSeconds(getTimeout());
    }

    /**
     * Gets the slow motion execution delay in milliseconds (useful for demo/debugging).
     *
     * @return delay in milliseconds, defaults to 0
     */
    public static int getSlowMo() {
        String slowMo = getProperty("slowMo");
        try {
            return (slowMo != null && !slowMo.trim().isEmpty()) ? Integer.parseInt(slowMo.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
