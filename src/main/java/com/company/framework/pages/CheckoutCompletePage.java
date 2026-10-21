package com.company.framework.pages;

import com.company.framework.base.BasePage;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object representing the final Order Confirmation page (/checkout-complete.html).
 */
public class CheckoutCompletePage extends BasePage {

    private final HeaderComponent header;
    private final By completeHeader = By.className("complete-header");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage() {
        super();
        this.header = new HeaderComponent();
        WaitUtils.waitForElementVisible(driver, backHomeButton);
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
     * Retrieves the confirmation greeting header text (e.g., "Thank you for your order!").
     *
     * @return confirmation text
     */
    @Step("Get confirmation header text")
    public String getConfirmationHeader() {
        return getText(completeHeader);
    }

    /**
     * Clicks the Back Home button and returns to the inventory catalog.
     *
     * @return {@link InventoryPage} instance
     */
    @Step("Click Back Home button")
    public InventoryPage clickBackHome() {
        click(backHomeButton);
        return new InventoryPage();
    }
}
