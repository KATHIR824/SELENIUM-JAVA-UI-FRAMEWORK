package com.company.framework.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-safe wrapper for managing {@link WebDriver} instances across parallel test executions.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Prevent instantiation
    }

    /**
     * Retrieves the current thread's {@link WebDriver} instance.
     *
     * @return active {@link WebDriver}
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Binds a {@link WebDriver} instance to the current thread.
     *
     * @param driverRef the {@link WebDriver} instance
     */
    public static void setDriver(WebDriver driverRef) {
        driverThreadLocal.set(driverRef);
    }

    /**
     * Removes the {@link WebDriver} instance from the current thread to prevent memory leaks.
     */
    public static void unload() {
        driverThreadLocal.remove();
    }
}
