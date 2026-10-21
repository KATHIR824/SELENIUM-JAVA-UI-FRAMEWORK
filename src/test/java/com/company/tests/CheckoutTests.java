package com.company.tests;

import com.company.framework.base.BaseTest;
import com.company.framework.config.ConfigReader;
import com.company.framework.pages.CartPage;
import com.company.framework.pages.CheckoutCompletePage;
import com.company.framework.pages.CheckoutInfoPage;
import com.company.framework.pages.CheckoutOverviewPage;
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
 * Test class covering the complete checkout workflow including validation and price calculations.
 */
@Epic("Checkout Flow")
@Feature("Checkout Feature")
public class CheckoutTests extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Story("Complete End to End Checkout")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can complete a purchase from adding item to cart through order completion")
    public void completeCheckoutEndToEnd() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = inventoryPage.openCart();
        CheckoutInfoPage infoPage = cartPage.clickCheckout();

        CheckoutOverviewPage overviewPage = infoPage.enterInformation("John", "Doe", "12345");
        CheckoutCompletePage completePage = overviewPage.clickFinish();

        Assert.assertEquals(completePage.getConfirmationHeader(), "Thank you for your order!",
                "Order completion header mismatch");
    }

    @Test(dataProvider = "invalidCheckoutData", groups = {"regression"})
    @Story("Checkout Validation Errors")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify validation error messages when submitting missing customer information during checkout")
    public void checkoutWithMissingInformation(String scenario, String firstName, String lastName, String postalCode, String expectedError) {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = inventoryPage.openCart();
        CheckoutInfoPage infoPage = cartPage.clickCheckout();

        infoPage.enterInvalidInformation(firstName, lastName, postalCode);
        Assert.assertEquals(infoPage.getErrorMessage(), expectedError,
                "Error message mismatch for scenario: " + scenario);
    }

    @Test(groups = {"regression"})
    @Story("Cancel Checkout Navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that clicking cancel on the checkout information page navigates back to /cart.html")
    public void cancelCheckoutReturnsToCart() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = inventoryPage.openCart();
        CheckoutInfoPage infoPage = cartPage.clickCheckout();

        CartPage returnedCart = infoPage.clickCancel();
        Assert.assertTrue(returnedCart.getCurrentUrl().contains("/cart.html"),
                "Current URL should contain '/cart.html' after cancelling checkout. Found: " + returnedCart.getCurrentUrl());
        Assert.assertTrue(returnedCart.isOnCartPage(), "User should be on the Cart Page");
    }

    @Test(groups = {"regression"})
    @Story("Checkout Overview Total Price Calculation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the item subtotal plus tax precisely equals the total amount on the overview page")
    public void checkoutOverviewTotalIsCorrect() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.addProductToCart("Sauce Labs Bike Light");

        CartPage cartPage = inventoryPage.openCart();
        CheckoutInfoPage infoPage = cartPage.clickCheckout();
        CheckoutOverviewPage overviewPage = infoPage.enterInformation("John", "Doe", "12345");

        double subtotal = overviewPage.getSubtotal();
        double tax = overviewPage.getTax();
        double total = overviewPage.getTotal();
        double calculatedTotal = Math.round((subtotal + tax) * 100.0) / 100.0;

        Assert.assertTrue(subtotal > 0, "Subtotal must be greater than 0");
        Assert.assertTrue(tax > 0, "Tax must be greater than 0");
        Assert.assertEquals(total, calculatedTotal,
                "Total price ($" + total + ") does not equal calculated Subtotal ($" + subtotal + ") + Tax ($" + tax + ")");
    }

    @DataProvider(name = "invalidCheckoutData")
    public Object[][] getInvalidCheckoutData() {
        List<Map<String, String>> data = JsonUtils.getJsonData("testdata/checkout.json");
        Object[][] testData = new Object[data.size()][5];
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i).get("scenario");
            testData[i][1] = data.get(i).get("firstName");
            testData[i][2] = data.get(i).get("lastName");
            testData[i][3] = data.get(i).get("postalCode");
            testData[i][4] = data.get(i).get("expectedError");
        }
        return testData;
    }
}
