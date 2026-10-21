package com.company.framework.pages;

import com.company.framework.base.BasePage;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object representing the Shopping Cart page (/cart.html).
 */
public class CartPage extends BasePage {

    private final HeaderComponent header;
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");
    private final By cartList = By.className("cart_list");

    public CartPage() {
        super();
        this.header = new HeaderComponent();
        WaitUtils.waitForElementVisible(driver, checkoutButton);
    }

    /**
     * Retrieves the header component.
     *
     * @return {@link HeaderComponent}
     */
    public HeaderComponent getHeader() {
        return header;
    }

    /**
     * Checks if user is on the cart page.
     *
     * @return true if on cart page, false otherwise
     */
    @Step("Check if on Cart Page")
    public boolean isOnCartPage() {
        return isDisplayed(checkoutButton);
    }

    /**
     * Retrieves the count of items in the cart list without blocking if empty.
     *
     * @return number of items in cart
     */
    @Step("Get cart item count")
    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    /**
     * Retrieves the list of item names currently in the cart.
     *
     * @return list of product name strings
     */
    @Step("Get all cart item names")
    public List<String> getCartItemNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> elements = driver.findElements(itemNames);
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }

    /**
     * Removes an item from the cart by its name.
     *
     * @param productName name of the product to remove
     * @return this {@link CartPage} instance
     */
    @Step("Remove item from cart: {0}")
    public CartPage removeItem(String productName) {
        String idName = productName.toLowerCase().replace(" ", "-");
        By removeButton = By.id("remove-" + idName);
        click(removeButton);
        return this;
    }

    /**
     * Clicks the Continue Shopping button and navigates back to the inventory.
     *
     * @return {@link InventoryPage} instance
     */
    @Step("Click continue shopping")
    public InventoryPage clickContinueShopping() {
        click(continueShoppingButton);
        return new InventoryPage();
    }

    /**
     * Clicks the Checkout button to begin the checkout process.
     *
     * @return {@link CheckoutInfoPage} instance
     */
    @Step("Click checkout")
    public CheckoutInfoPage clickCheckout() {
        click(checkoutButton);
        return new CheckoutInfoPage();
    }
}
