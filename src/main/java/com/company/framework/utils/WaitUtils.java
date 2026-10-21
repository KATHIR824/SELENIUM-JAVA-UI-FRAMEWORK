package com.company.framework.utils;

import com.company.framework.config.ConfigReader;
import com.company.framework.exceptions.FrameworkException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Utility class providing explicit wait mechanisms using Selenium's {@link WebDriverWait}.
 */
public final class WaitUtils {

    private WaitUtils() {
        // Prevent instantiation
    }

    private static Duration getDefaultTimeout() {
        return ConfigReader.getTimeoutDuration();
    }

    /**
     * Waits until the element located by the given locator is visible on the page.
     *
     * @param driver  the {@link WebDriver} instance
     * @param locator the element locator
     * @return the visible {@link WebElement}
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        return waitForElementVisible(driver, locator, getDefaultTimeout());
    }

    /**
     * Waits until the element located by the given locator is visible within the specified timeout.
     *
     * @param driver  the {@link WebDriver} instance
     * @param locator the element locator
     * @param timeout custom timeout duration
     * @return the visible {@link WebElement}
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator, Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            throw new FrameworkException("Element was not visible within " + timeout.getSeconds() + "s: " + locator, e);
        }
    }

    /**
     * Waits until all elements matching the given locator are visible on the page.
     *
     * @param driver  the {@link WebDriver} instance
     * @param locator the element locator
     * @return the list of visible {@link WebElement} instances
     */
    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, By locator) {
        return waitForAllElementsVisible(driver, locator, getDefaultTimeout());
    }

    /**
     * Waits until all elements matching the given locator are visible within the specified timeout.
     *
     * @param driver  the {@link WebDriver} instance
     * @param locator the element locator
     * @param timeout custom timeout duration
     * @return the list of visible {@link WebElement} instances
     */
    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, By locator, Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (Exception e) {
            throw new FrameworkException("Elements were not all visible within " + timeout.getSeconds() + "s: " + locator, e);
        }
    }

    /**
     * Waits until the element located by the given locator is clickable.
     *
     * @param driver  the {@link WebDriver} instance
     * @param locator the element locator
     * @return the clickable {@link WebElement}
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        return waitForElementClickable(driver, locator, getDefaultTimeout());
    }

    /**
     * Waits until the element located by the given locator is clickable within the specified timeout.
     *
     * @param driver  the {@link WebDriver} instance
     * @param locator the element locator
     * @param timeout custom timeout duration
     * @return the clickable {@link WebElement}
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator, Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            throw new FrameworkException("Element was not clickable within " + timeout.getSeconds() + "s: " + locator, e);
        }
    }

    /**
     * Waits until the element located by the given locator becomes invisible or detached from the DOM.
     *
     * @param driver  the {@link WebDriver} instance
     * @param locator the element locator
     * @return true if the element is invisible, false if timeout expires
     */
    public static boolean waitForElementInvisible(WebDriver driver, By locator) {
        return waitForElementInvisible(driver, locator, getDefaultTimeout());
    }

    /**
     * Waits until the element located by the given locator becomes invisible within the specified timeout.
     *
     * @param driver  the {@link WebDriver} instance
     * @param locator the element locator
     * @param timeout custom timeout duration
     * @return true if the element is invisible, false if timeout expires
     */
    public static boolean waitForElementInvisible(WebDriver driver, By locator, Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            return Boolean.TRUE.equals(wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits until the current URL contains the expected fraction.
     *
     * @param driver        the {@link WebDriver} instance
     * @param fractionInUrl fraction of the URL expected
     * @return true if the URL contains the fraction, false otherwise
     */
    public static boolean waitForUrlContains(WebDriver driver, String fractionInUrl) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, getDefaultTimeout());
            return Boolean.TRUE.equals(wait.until(ExpectedConditions.urlContains(fractionInUrl)));
        } catch (Exception e) {
            return false;
        }
    }
}
