package com.company.framework.pages;

import com.company.framework.base.BasePage;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object representing the product sorting and filtering operations on the inventory page.
 */
public class ProductSortPage extends BasePage {

    private final HeaderComponent header;
    private final By sortDropdown = By.className("product_sort_container");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By itemNames = By.cssSelector(".inventory_item_name");

    public ProductSortPage() {
        super();
        this.header = new HeaderComponent();
        WaitUtils.waitForElementVisible(driver, sortDropdown);
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
     * Sorts catalog items using the dropdown option value (e.g., 'az', 'za', 'lohi', 'hilo').
     *
     * @param sortValue sort option value attribute
     * @return this {@link ProductSortPage} instance
     */
    @Step("Sort products by value: {0}")
    public ProductSortPage sortItems(String sortValue) {
        selectDropdownByValue(sortDropdown, sortValue);
        return this;
    }

    /**
     * Retrieves all product prices in their current displayed order.
     *
     * @return list of parsed price values as {@link Double}
     */
    @Step("Get all sorted product prices")
    public List<Double> getAllItemPrices() {
        List<WebElement> elements = WaitUtils.waitForAllElementsVisible(driver, itemPrices);
        List<Double> prices = new ArrayList<>();
        for (WebElement element : elements) {
            String priceText = element.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }

    /**
     * Retrieves all product names in their current displayed order.
     *
     * @return list of product name strings
     */
    @Step("Get all sorted product names")
    public List<String> getAllItemNames() {
        List<WebElement> elements = WaitUtils.waitForAllElementsVisible(driver, itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }
}
