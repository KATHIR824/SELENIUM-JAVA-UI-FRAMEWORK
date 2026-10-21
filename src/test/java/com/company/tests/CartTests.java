package com.company.tests;

import com.company.framework.base.BaseTest;
import com.company.framework.config.ConfigReader;
import com.company.framework.pages.CartPage;
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
 * Test class covering cart operations such as adding, removing, and verifying cart items.
 */
@Epic("Shopping Cart")
@Feature("Cart Management Feature")
public class CartTests extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Story("Add single product to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a single product updates the cart badge and displays the item in the cart page")
    public void addSingleProductToCart() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        String productName = "Sauce Labs Backpack";
        inventoryPage.addProductToCart(productName);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Cart badge count should display 1 after adding one item");

        CartPage cartPage = inventoryPage.openCart();
        List<String> items = cartPage.getCartItemNames();

        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart page item count should equal 1");
        Assert.assertEquals(items, List.of(productName), "Cart page must list the exact single product added: " + productName);
    }

    @Test(groups = {"regression"})
    @Story("Add multiple products to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding multiple products updates the badge count and displays all items on the cart page")
    public void addMultipleProductsToCart() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        String product1 = "Sauce Labs Backpack";
        String product2 = "Sauce Labs Bike Light";

        inventoryPage.addProductToCart(product1);
        inventoryPage.addProductToCart(product2);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart badge count should display 2 after adding two items");

        CartPage cartPage = inventoryPage.openCart();
        List<String> items = cartPage.getCartItemNames();

        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart page item count should equal 2");
        Assert.assertEquals(items, List.of(product1, product2),
                "Cart page must list all exact products added in order: " + List.of(product1, product2));
    }

    @Test(groups = {"regression"})
    @Story("Remove product from cart on inventory page")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify removing a product directly from the inventory page removes the cart badge immediately")
    public void removeProductFromCartOnHomePage() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        String product = "Sauce Labs Backpack";
        inventoryPage.addProductToCart(product);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Cart badge count should be 1 after adding product");

        inventoryPage.removeProductFromCart(product);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 0, "Cart badge count should be 0 after removing product");
        Assert.assertFalse(inventoryPage.getHeader().isCartBadgeDisplayed(), "Cart badge element should no longer be displayed");
    }

    @Test(groups = {"regression"})
    @Story("Remove product from cart page")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify removing a product from within the cart page clears the item and removes the badge")
    public void removeProductFromCartPage() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        String product = "Sauce Labs Backpack";
        inventoryPage.addProductToCart(product);

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart page should initially have 1 item");

        cartPage.removeItem(product);
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart item list should be empty after removal");
        Assert.assertEquals(cartPage.getHeader().getCartBadgeCount(), 0, "Cart badge count should be 0");
        Assert.assertFalse(cartPage.getHeader().isCartBadgeDisplayed(), "Cart badge should not be visible when cart is empty");
    }
}
