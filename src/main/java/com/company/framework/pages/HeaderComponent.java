package com.company.framework.pages;

import com.company.framework.base.BasePage;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Reusable Header component containing navigation items, the shopping cart badge, and the hamburger menu.
 */
public class HeaderComponent extends BasePage {

    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By appLogo = By.className("app_logo");

    /**
     * Opens the burger sidebar menu.
     */
    @Step("Open navigation burger menu")
    public HeaderComponent openMenu() {
        click(menuButton);
        WaitUtils.waitForElementVisible(driver, logoutLink);
        return this;
    }

    /**
     * Performs logout via the navigation menu.
     *
     * @return {@link LoginPage} instance
     */
    @Step("Click logout from navigation menu")
    public LoginPage logout() {
        openMenu();
        WaitUtils.waitForElementClickable(driver, logoutLink);
        click(logoutLink);
        return new LoginPage();
    }

    /**
     * Opens the shopping cart page.
     *
     * @return {@link CartPage} instance
     */
    @Step("Open shopping cart page")
    public CartPage openCart() {
        click(cartLink);
        return new CartPage();
    }

    /**
     * Checks if the shopping cart item count badge is currently displayed in the DOM.
     *
     * @return true if displayed, false otherwise
     */
    @Step("Check if shopping cart badge is displayed")
    public boolean isCartBadgeDisplayed() {
        List<WebElement> elements = driver.findElements(cartBadge);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    /**
     * Retrieves the current numeric badge count for the shopping cart.
     * Returns 0 immediately if the badge is absent without blocking.
     *
     * @return cart badge count
     */
    @Step("Get cart badge count")
    public int getCartBadgeCount() {
        List<WebElement> elements = driver.findElements(cartBadge);
        if (elements.isEmpty() || !elements.get(0).isDisplayed()) {
            return 0;
        }
        try {
            return Integer.parseInt(elements.get(0).getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Retrieves the text displayed on the application header logo.
     *
     * @return app logo text
     */
    @Step("Get application logo text")
    public String getAppLogoText() {
        return getText(appLogo);
    }
}
