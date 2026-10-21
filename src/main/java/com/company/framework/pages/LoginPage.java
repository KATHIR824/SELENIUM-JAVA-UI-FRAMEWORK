package com.company.framework.pages;

import com.company.framework.base.BasePage;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object representing the SauceDemo Login page (/ or /index.html).
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage() {
        super();
        WaitUtils.waitForElementVisible(driver, loginButton);
    }

    /**
     * Performs a valid login with credentials and navigates to the {@link InventoryPage}.
     *
     * @param username user login name
     * @param password user login password
     * @return {@link InventoryPage} instance
     */
    @Step("Login with username: {0}")
    public InventoryPage login(String username, String password) {
        type(usernameInput, username);
        typeMasked(passwordInput, password);
        click(loginButton);
        return new InventoryPage();
    }

    /**
     * Performs an invalid login attempt with given credentials.
     *
     * @param username user login name
     * @param password user login password
     * @return this {@link LoginPage} instance
     */
    @Step("Attempt invalid login with username: {0}")
    public LoginPage loginInvalid(String username, String password) {
        type(usernameInput, username);
        typeMasked(passwordInput, password);
        click(loginButton);
        return this;
    }

    /**
     * Retrieves the error message displayed on an unsuccessful login attempt.
     *
     * @return error message text
     */
    @Step("Get login error message")
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
