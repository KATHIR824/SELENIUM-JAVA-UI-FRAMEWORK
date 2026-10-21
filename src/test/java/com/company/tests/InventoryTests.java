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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class covering the product catalog and inventory display functionality.
 */
@Epic("Inventory")
@Feature("Inventory Display Feature")
public class InventoryTests extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Story("Page Title Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the application page title is 'Swag Labs'")
    public void pageTitleValidationTest() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        Assert.assertEquals(inventoryPage.getTitle(), "Swag Labs", "Page title should equal 'Swag Labs'");
    }

    @Test(groups = {"smoke", "regression"})
    @Story("Product Count Verification")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that exactly 6 products are displayed on the inventory page")
    public void productCountIsSix() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        int count = inventoryPage.getProductCount();
        Assert.assertEquals(count, 6, "Inventory product catalog count must equal exactly 6");
    }

    @Test(groups = {"regression"})
    @Story("Product Details Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that every product on the inventory page has a non-empty name and a price greater than 0")
    public void allProductsHavePriceAndName() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        List<String> names = inventoryPage.getAllProductNames();
        List<Double> prices = inventoryPage.getAllProductPrices();

        // 1. First assert that the product lists are not empty
        Assert.assertNotNull(names, "Product names list should not be null");
        Assert.assertFalse(names.isEmpty(), "Product names list must not be empty");
        Assert.assertNotNull(prices, "Product prices list should not be null");
        Assert.assertFalse(prices.isEmpty(), "Product prices list must not be empty");

        // 2. Assert exact catalog size
        Assert.assertEquals(names.size(), 6, "Expected exactly 6 product names to be loaded");
        Assert.assertEquals(prices.size(), 6, "Expected exactly 6 product prices to be loaded");

        // 3. Assert each item has a non-empty name and positive price
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            Double price = prices.get(i);

            Assert.assertNotNull(name, "Product name at index " + i + " should not be null");
            Assert.assertFalse(name.trim().isEmpty(), "Product name at index " + i + " should not be empty or blank");
            Assert.assertNotNull(price, "Product price at index " + i + " should not be null");
            Assert.assertTrue(price > 0.0, "Product '" + name + "' price ($" + price + ") must be greater than 0");
        }
    }
}
