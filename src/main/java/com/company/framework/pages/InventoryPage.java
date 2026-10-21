package com.company.framework.pages;

import com.company.framework.base.BasePage;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object representing the Inventory (Products Catalog) page (/inventory.html).
 */
public class InventoryPage extends BasePage {

    private final HeaderComponent header;
    private final By inventoryList = By.className("inventory_list");
    private final By productsTitle = By.cssSelector(".title");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");

    public InventoryPage() {
        super();
        this.header = new HeaderComponent();
        WaitUtils.waitForElementVisible(driver, inventoryList);
    }

    /**
     * Gets the page header component.
     *
     * @return {@link HeaderComponent}
     */
    public HeaderComponent getHeader() {
        return header;
    }

    /**
     * Checks if the inventory page is currently displayed.
     *
     * @return true if on inventory page, false otherwise
     */
    @Step("Check if on Inventory Page")
    public boolean isOnInventoryPage() {
        return isDisplayed(productsTitle);
    }

    /**
     * Alias for {@link #isOnInventoryPage()} for compatibility.
     *
     * @return true if on inventory page
     */
    @Step("Check if on Home/Inventory Page")
    public boolean isOnHomePage() {
        return isOnInventoryPage();
    }

    /**
     * Retrieves the inventory page title header text.
     *
     * @return title string (e.g. "Products")
     */
    @Step("Get Products Title")
    public String getProductsTitle() {
        return getText(productsTitle);
    }

    /**
     * Performs logout via the header menu.
     *
     * @return {@link LoginPage} instance
     */
    @Step("Logout from application")
    public LoginPage logout() {
        return header.logout();
    }

    /**
     * Navigates to the product sorting and filtering view.
     *
     * @return {@link ProductSortPage} instance
     */
    @Step("Go to Product Sort Page")
    public ProductSortPage goToProductSortPage() {
        return new ProductSortPage();
    }

    /**
     * Compatibility method for navigating to sort/filter page.
     *
     * @return {@link ProductSortPage} instance
     */
    @Step("Go to Search/Sort Page")
    public ProductSortPage goToSearchPage() {
        return goToProductSortPage();
    }

    /**
     * Adds a product to the cart by its displayed name.
     *
     * @param productName name of the product to add
     * @return this {@link InventoryPage} instance
     */
    @Step("Add product to cart: {0}")
    public InventoryPage addProductToCart(String productName) {
        String idName = productName.toLowerCase().replace(" ", "-");
        By addToCartBtn = By.id("add-to-cart-" + idName);
        click(addToCartBtn);
        return this;
    }

    /**
     * Removes a product from the cart directly on the inventory page.
     *
     * @param productName name of the product to remove
     * @return this {@link InventoryPage} instance
     */
    @Step("Remove product from cart: {0}")
    public InventoryPage removeProductFromCart(String productName) {
        String idName = productName.toLowerCase().replace(" ", "-");
        By removeBtn = By.id("remove-" + idName);
        click(removeBtn);
        return this;
    }

    /**
     * Retrieves the shopping cart badge count from the header.
     *
     * @return item count or 0 if no badge is present
     */
    @Step("Get cart badge count")
    public int getCartBadgeCount() {
        return header.getCartBadgeCount();
    }

    /**
     * Opens the shopping cart page.
     *
     * @return {@link CartPage} instance
     */
    @Step("Open shopping cart")
    public CartPage openCart() {
        return header.openCart();
    }

    /**
     * Gets the total number of products displayed in the catalog.
     * Waits explicitly until all inventory item elements are visible in the DOM.
     *
     * @return count of displayed products
     */
    @Step("Get product count")
    public int getProductCount() {
        List<WebElement> elements = WaitUtils.waitForAllElementsVisible(driver, inventoryItems);
        return elements.size();
    }

    /**
     * Retrieves all product names displayed on the inventory page.
     * Waits explicitly until all product item name elements are visible.
     *
     * @return list of product names
     */
    @Step("Get all product names")
    public List<String> getAllProductNames() {
        List<WebElement> elements = WaitUtils.waitForAllElementsVisible(driver, itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }

    /**
     * Retrieves all product prices displayed on the inventory page.
     * Waits explicitly until all product item price elements are visible.
     *
     * @return list of product prices as {@link Double}
     */
    @Step("Get all product prices")
    public List<Double> getAllProductPrices() {
        List<WebElement> elements = WaitUtils.waitForAllElementsVisible(driver, itemPrices);
        List<Double> prices = new ArrayList<>();
        for (WebElement element : elements) {
            String priceText = element.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }
}
