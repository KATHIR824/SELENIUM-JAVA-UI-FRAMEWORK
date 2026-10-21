package com.company.framework.base;

import com.company.framework.driver.DriverManager;
import com.company.framework.exceptions.FrameworkException;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;

/**
 * Abstract BasePage representing common interactions across all Page Objects in the Page Object Model.
 * Encapsulates WebDriver actions, explicit waits, logging, and Allure reporting steps.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected Logger logger = LogManager.getLogger(this.getClass());

    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    /**
     * Navigates to the specified URL.
     *
     * @param url the destination URL
     */
    @Step("Open URL: {0}")
    public void open(String url) {
        logger.info("Opening URL: {}", url);
        driver.get(url);
    }

    /**
     * Retrieves the current browser window title.
     *
     * @return page title string
     */
    @Step("Get Page Title")
    public String getTitle() {
        String title = driver.getTitle();
        logger.info("Retrieved page title: '{}'", title);
        return title;
    }

    /**
     * Retrieves the current browser URL.
     *
     * @return current URL string
     */
    @Step("Get Current URL")
    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        logger.info("Current URL: {}", currentUrl);
        return currentUrl;
    }

    /**
     * Clicks on the element specified by the locator after ensuring it is clickable.
     *
     * @param locator the element By locator
     */
    @Step("Click on element: {0}")
    public void click(By locator) {
        try {
            logger.info("Clicking on element: {}", locator);
            WebElement element = WaitUtils.waitForElementClickable(driver, locator);
            element.click();
        } catch (Exception e) {
            throw new FrameworkException("Failed to click element: " + locator, e);
        }
    }

    /**
     * Clears and types text into the specified element after ensuring it is visible.
     *
     * @param locator the element By locator
     * @param text    the text to send
     */
    @Step("Type text '{1}' in element: {0}")
    public void type(By locator, String text) {
        try {
            logger.info("Typing text '{}' in element: {}", text, locator);
            WebElement element = WaitUtils.waitForElementVisible(driver, locator);
            element.clear();
            element.sendKeys(text != null ? text : "");
        } catch (Exception e) {
            throw new FrameworkException("Failed to type into element: " + locator, e);
        }
    }

    /**
     * Clears and types sensitive text (e.g. passwords) with masked logging output.
     *
     * @param locator the element By locator
     * @param text    the sensitive text to send
     */
    @Step("Type masked text in element: {0}")
    public void typeMasked(By locator, String text) {
        try {
            logger.info("Typing text '****' in element: {}", locator);
            WebElement element = WaitUtils.waitForElementVisible(driver, locator);
            element.clear();
            element.sendKeys(text != null ? text : "");
        } catch (Exception e) {
            throw new FrameworkException("Failed to type masked text into element: " + locator, e);
        }
    }

    /**
     * Retrieves the visible text of an element after ensuring it is visible.
     *
     * @param locator the element By locator
     * @return text contained within the element
     */
    @Step("Get text from element: {0}")
    public String getText(By locator) {
        try {
            logger.info("Getting text from element: {}", locator);
            WebElement element = WaitUtils.waitForElementVisible(driver, locator);
            return element.getText();
        } catch (Exception e) {
            throw new FrameworkException("Failed to get text from element: " + locator, e);
        }
    }

    /**
     * Checks immediately whether an element is present and visible without waiting for a long timeout.
     *
     * @param locator the element By locator
     * @return true if element is present in DOM and displayed, false otherwise
     */
    @Step("Check if element is displayed: {0}")
    public boolean isDisplayed(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Selects an option from a dropdown by visible text.
     *
     * @param locator the select element By locator
     * @param text    visible option text
     */
    @Step("Select dropdown by text '{1}' for element: {0}")
    public void selectDropdownByText(By locator, String text) {
        try {
            logger.info("Selecting dropdown option '{}' on element: {}", text, locator);
            Select select = new Select(WaitUtils.waitForElementVisible(driver, locator));
            select.selectByVisibleText(text);
        } catch (Exception e) {
            throw new FrameworkException("Failed to select dropdown option by text: " + locator, e);
        }
    }

    /**
     * Selects an option from a dropdown by attribute value.
     *
     * @param locator the select element By locator
     * @param value   option value attribute
     */
    @Step("Select dropdown by value '{1}' for element: {0}")
    public void selectDropdownByValue(By locator, String value) {
        try {
            logger.info("Selecting dropdown option value '{}' on element: {}", value, locator);
            Select select = new Select(WaitUtils.waitForElementVisible(driver, locator));
            select.selectByValue(value);
        } catch (Exception e) {
            throw new FrameworkException("Failed to select dropdown option by value: " + locator, e);
        }
    }

    /**
     * Selects an option from a dropdown by index.
     *
     * @param locator the select element By locator
     * @param index   zero-based index
     */
    @Step("Select dropdown by index '{1}' for element: {0}")
    public void selectDropdownByIndex(By locator, int index) {
        try {
            logger.info("Selecting dropdown option at index {} on element: {}", index, locator);
            Select select = new Select(WaitUtils.waitForElementVisible(driver, locator));
            select.selectByIndex(index);
        } catch (Exception e) {
            throw new FrameworkException("Failed to select dropdown option by index: " + locator, e);
        }
    }

    /**
     * Hovers the mouse cursor over the specified element.
     *
     * @param locator the element By locator
     */
    @Step("Hover over element: {0}")
    public void hover(By locator) {
        try {
            logger.info("Hovering over element: {}", locator);
            Actions actions = new Actions(driver);
            WebElement element = WaitUtils.waitForElementVisible(driver, locator);
            actions.moveToElement(element).perform();
        } catch (Exception e) {
            throw new FrameworkException("Failed to hover over element: " + locator, e);
        }
    }

    /**
     * Uploads a file to an input element.
     *
     * @param locator  the input element By locator
     * @param filePath path to the file to upload
     */
    @Step("Upload file '{1}' to element: {0}")
    public void uploadFile(By locator, String filePath) {
        try {
            logger.info("Uploading file '{}' to element: {}", filePath, locator);
            File file = new File(filePath);
            WebElement element = driver.findElement(locator);
            element.sendKeys(file.getAbsolutePath());
        } catch (Exception e) {
            throw new FrameworkException("Failed to upload file to element: " + locator, e);
        }
    }

    /**
     * Refreshes the current web page.
     */
    @Step("Refresh page")
    public void refresh() {
        logger.info("Refreshing current page");
        driver.navigate().refresh();
    }

    /**
     * Navigates back in browser history.
     */
    @Step("Navigate back")
    public void back() {
        logger.info("Navigating back to previous page");
        driver.navigate().back();
    }

    /**
     * Scrolls the window viewport until the target element is visible.
     *
     * @param locator the element By locator
     */
    @Step("Scroll to element: {0}")
    public void scrollToElement(By locator) {
        try {
            logger.info("Scrolling to element: {}", locator);
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            throw new FrameworkException("Failed to scroll to element: " + locator, e);
        }
    }

    /**
     * Executes a JavaScript click on the target element.
     *
     * @param locator the element By locator
     */
    @Step("JavaScript click on element: {0}")
    public void jsClick(By locator) {
        try {
            logger.info("JavaScript clicking element: {}", locator);
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            throw new FrameworkException("Failed to JS click element: " + locator, e);
        }
    }

    /**
     * Switches into the specified iframe.
     *
     * @param locator the iframe element By locator
     */
    @Step("Switch to frame: {0}")
    public void switchToFrame(By locator) {
        try {
            logger.info("Switching to iframe: {}", locator);
            WebElement element = WaitUtils.waitForElementVisible(driver, locator);
            driver.switchTo().frame(element);
        } catch (Exception e) {
            throw new FrameworkException("Failed to switch to frame: " + locator, e);
        }
    }

    /**
     * Switches to the window identified by the given window handle.
     *
     * @param windowHandle window handle string
     */
    @Step("Switch to window: {0}")
    public void switchToWindow(String windowHandle) {
        try {
            logger.info("Switching to window handle: {}", windowHandle);
            driver.switchTo().window(windowHandle);
        } catch (Exception e) {
            throw new FrameworkException("Failed to switch to window: " + windowHandle, e);
        }
    }

    /**
     * Accepts any currently displayed browser alert dialog.
     */
    @Step("Accept alert")
    public void handleAlert() {
        try {
            logger.info("Accepting browser alert");
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            throw new FrameworkException("Failed to handle browser alert", e);
        }
    }

    /**
     * Waits explicitly until the element is visible.
     *
     * @param locator the element By locator
     */
    public void waitForElementVisible(By locator) {
        WaitUtils.waitForElementVisible(driver, locator);
    }

    /**
     * Waits explicitly until the element is clickable.
     *
     * @param locator the element By locator
     */
    public void waitForElementClickable(By locator) {
        WaitUtils.waitForElementClickable(driver, locator);
    }
}
