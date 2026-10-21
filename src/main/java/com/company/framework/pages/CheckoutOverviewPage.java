package com.company.framework.pages;

import com.company.framework.base.BasePage;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object representing the second step of checkout: Order Overview page (/checkout-step-two.html).
 */
public class CheckoutOverviewPage extends BasePage {

    private final HeaderComponent header;
    private final By itemNames = By.className("inventory_item_name");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutOverviewPage() {
        super();
        this.header = new HeaderComponent();
        WaitUtils.waitForElementVisible(driver, finishButton);
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
     * Retrieves the list of item names displayed in the checkout overview.
     *
     * @return list of item names
     */
    @Step("Get all overview item names")
    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> elements = driver.findElements(itemNames);
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }

    /**
     * Retrieves and parses the item subtotal price amount.
     *
     * @return subtotal amount as {@link Double}
     */
    @Step("Get subtotal amount")
    public double getSubtotal() {
        String text = getText(subtotalLabel);
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Retrieves and parses the calculated tax price amount.
     *
     * @return tax amount as {@link Double}
     */
    @Step("Get tax amount")
    public double getTax() {
        String text = getText(taxLabel);
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Retrieves and parses the total price amount.
     *
     * @return total amount as {@link Double}
     */
    @Step("Get total amount")
    public double getTotal() {
        String text = getText(totalLabel);
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Clicks the Finish button to place the order.
     *
     * @return {@link CheckoutCompletePage} instance
     */
    @Step("Click Finish button")
    public CheckoutCompletePage clickFinish() {
        click(finishButton);
        return new CheckoutCompletePage();
    }

    /**
     * Clicks the Cancel button and returns to the inventory catalog.
     *
     * @return {@link InventoryPage} instance
     */
    @Step("Click Cancel button")
    public InventoryPage clickCancel() {
        click(cancelButton);
        return new InventoryPage();
    }
}
