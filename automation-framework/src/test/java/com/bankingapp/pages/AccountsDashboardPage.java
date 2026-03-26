package test.java.com.bankingapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountsDashboardPage {
    private WebDriver driver;

    // Locators
    private By overviewHeader = By.xpath("//h2[text()='Accounts Overview']");
    private By createAccountButton = By.id("createAccBtn");

    public AccountsDashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isOverviewHeaderVisible() {
        return driver.findElement(overviewHeader).isDisplayed();
    }

    public boolean isCreateAccountFormVisible() {
        return driver.findElement(createAccountButton).isDisplayed();
    }

    // New Locators for Account Creation
    private By accNameInput = By.id("accName");
    private By accDepositInput = By.id("accDeposit");
    private By submitAccountBtn = By.xpath("//button[text()='Create Account']");
    
    // New Locators for Transactions
    private By transAccIdInput = By.id("transAccId");
    private By transAmountInput = By.id("transAmount");
    private By submitTransBtn = By.xpath("//form[@id='transactionForm']//button");

    public void createCheckingAccount(String name, String deposit) {
        driver.findElement(accNameInput).sendKeys(name);
        driver.findElement(accDepositInput).sendKeys(deposit);
        driver.findElement(submitAccountBtn).click();
    }

    public void depositFunds(String accId, String amount) {
        driver.findElement(transAccIdInput).sendKeys(accId);
        driver.findElement(transAmountInput).sendKeys(amount);
        driver.findElement(submitTransBtn).click();
    }
}
