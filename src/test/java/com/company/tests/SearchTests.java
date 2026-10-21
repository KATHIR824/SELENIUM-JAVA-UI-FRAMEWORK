package com.company.tests;

import com.company.framework.base.BaseTest;
import com.company.framework.config.ConfigReader;
import com.company.framework.pages.InventoryPage;
import com.company.framework.pages.LoginPage;
import com.company.framework.pages.ProductSortPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class covering catalog sorting and filtering functionality.
 */
@Epic("Inventory")
@Feature("Product Sort and Filter Feature")
public class SearchTests extends BaseTest {

    @Test(groups = {"regression"})
    @Story("Sort Products Price Low to High")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that products are sorted in ascending order when 'Price (low to high)' option is selected")
    public void sortPriceLowToHighTest() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
        ProductSortPage sortPage = inventoryPage.goToProductSortPage();

        sortPage.sortItems("lohi");
        List<Double> actualPrices = sortPage.getAllItemPrices();

        Assert.assertFalse(actualPrices.isEmpty(), "Price list should not be empty after sorting");

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices,
                "Product prices are not sorted in ascending order (Low to High)");
    }

    @Test(groups = {"regression"})
    @Story("Sort Products Name Z to A")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products are sorted in reverse alphabetical order when 'Name (Z to A)' option is selected")
    public void sortNameZToATest() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
        ProductSortPage sortPage = inventoryPage.goToProductSortPage();

        sortPage.sortItems("za");
        List<String> actualNames = sortPage.getAllItemNames();

        Assert.assertFalse(actualNames.isEmpty(), "Product names list should not be empty after sorting");

        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));

        Assert.assertEquals(actualNames, expectedNames,
                "Product names are not sorted in reverse alphabetical order (Z to A)");
    }
}
