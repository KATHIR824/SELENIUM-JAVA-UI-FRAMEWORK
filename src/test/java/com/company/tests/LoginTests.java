package com.company.tests;

import com.company.framework.base.BaseTest;
import com.company.framework.config.ConfigReader;
import com.company.framework.pages.InventoryPage;
import com.company.framework.pages.LoginPage;
import com.company.framework.utils.JsonUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class covering user authentication flows on the SauceDemo portal.
 */
@Epic("Authentication")
@Feature("Login Feature")
public class LoginTests extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Story("Valid User Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can successfully log in with valid standard credentials")
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "User should be navigated to Inventory Page after valid login");
        Assert.assertEquals(inventoryPage.getProductsTitle(), "Products", "Header title should display 'Products'");
    }

    @Test(dataProvider = "invalidLoginData", groups = {"regression"})
    @Story("Invalid User Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that proper validation error messages are displayed for various invalid login attempts")
    public void invalidLoginTest(String scenario, String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage();
        loginPage.loginInvalid(username, password);

        Assert.assertEquals(loginPage.getErrorMessage(), expectedError,
                "Error message mismatch for scenario: " + scenario);
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        List<Map<String, String>> data = JsonUtils.getJsonData("testdata/login.json");
        Object[][] testData = new Object[data.size()][4];
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i).get("scenario");
            testData[i][1] = data.get(i).get("username");
            testData[i][2] = data.get(i).get("password");
            testData[i][3] = data.get(i).get("expectedError");
        }
        return testData;
    }
}
