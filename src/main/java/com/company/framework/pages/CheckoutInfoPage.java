package com.company.framework.pages;

import com.company.framework.base.BasePage;
import com.company.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object representing the first step of checkout: Customer Information page (/checkout-step-one.html).
 */
public class CheckoutInfoPage extends BasePage {

    private final HeaderComponent header;
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public CheckoutInfoPage() {
        super();
        this.header = new HeaderComponent();
        WaitUtils.waitForElementVisible(driver, firstNameInput);
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
     * Enters customer information and advances to the checkout overview page.
     *
     * @param firstName  customer first name
     * @param lastName   customer last name
     * @param postalCode customer postal code
     * @return {@link CheckoutOverviewPage} instance
     */
    @Step("Enter checkout information: First Name='{0}', Last Name='{1}', Postal Code='{2}'")
    public CheckoutOverviewPage enterInformation(String firstName, String lastName, String postalCode) {
        if (firstName != null && !firstName.isEmpty()) type(firstNameInput, firstName);
        if (lastName != null && !lastName.isEmpty()) type(lastNameInput, lastName);
        if (postalCode != null && !postalCode.isEmpty()) type(postalCodeInput, postalCode);
        click(continueButton);
        return new CheckoutOverviewPage();
    }

    /**
     * Enters customer information expecting a validation error without page navigation.
     *
     * @param firstName  customer first name
     * @param lastName   customer last name
     * @param postalCode customer postal code
     * @return this {@link CheckoutInfoPage} instance
     */
    @Step("Enter invalid checkout information: First Name='{0}', Last Name='{1}', Postal Code='{2}'")
    public CheckoutInfoPage enterInvalidInformation(String firstName, String lastName, String postalCode) {
        if (firstName != null && !firstName.isEmpty()) type(firstNameInput, firstName);
        if (lastName != null && !lastName.isEmpty()) type(lastNameInput, lastName);
        if (postalCode != null && !postalCode.isEmpty()) type(postalCodeInput, postalCode);
        click(continueButton);
        return this;
    }

    /**
     * Clicks the Cancel button and returns to the cart page.
     *
     * @return {@link CartPage} instance
     */
    @Step("Click Cancel button")
    public CartPage clickCancel() {
        click(cancelButton);
        return new CartPage();
    }

    /**
     * Retrieves the validation error message text.
     *
     * @return error message string
     */
    @Step("Get checkout error message")
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
