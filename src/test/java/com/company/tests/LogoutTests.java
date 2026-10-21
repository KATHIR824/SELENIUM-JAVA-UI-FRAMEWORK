package com.company.tests;

import com.company.framework.base.BaseTest;
import com.company.framework.config.ConfigReader;
import com.company.framework.pages.InventoryPage;
import com.company.framework.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class covering user logout and session termination behaviors.
 */
@Epic("Authentication")
@Feature("Logout Feature")
public class LogoutTests extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Story("User Logout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a logged in user can successfully log out via the side navigation menu")
    public void logoutSuccessfully() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        LoginPage returnedLogin = inventoryPage.logout();
        Assert.assertTrue(returnedLogin.isDisplayed(By.id("login-button")),
                "Login button should be visible on the login page after logout");
        Assert.assertTrue(returnedLogin.getCurrentUrl().contains("saucedemo.com"),
                "Browser URL should be on the SauceDemo login page");
    }

    @Test(groups = {"regression"})
    @Story("Unauthorized Inventory Access")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that accessing /inventory.html after logging out is blocked and displays an exact error message")
    public void cannotAccessInventoryAfterLogout() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
        LoginPage returnedLogin = inventoryPage.logout();

        returnedLogin.open(ConfigReader.getBaseUrl() + "/inventory.html");
        String errorMessage = returnedLogin.getErrorMessage();

        Assert.assertEquals(errorMessage,
                "Epic sadface: You can only access '/inventory.html' when you are logged in.",
                "Exact error message mismatch when attempting unauthorized inventory access after logout");
    }
}
