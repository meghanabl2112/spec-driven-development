package com.bankingapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountsDashboardPage {
    private WebDriver driver;

    // --- Locators ---
    private By overviewHeader = By.xpath("//h2[text()='Accounts Overview']");
    
    // Account Creation Locators
    private By accNameInput = By.id("accName");
    private By accDepositInput = By.id("accDeposit");
    private By submitAccountBtn = By.id("createAccBtn");
    
    // Transaction Locators (THESE ARE THE ONES YOU MISSED)
    private By firstAccountCard = By.className("account-card");
    private By transAmountInput = By.id("transAmount");
    private By submitTransBtn = By.id("submitTransBtn");

    // --- Constructor ---
    public AccountsDashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    // --- Methods ---
    public boolean isOverviewHeaderVisible() {
        return driver.findElement(overviewHeader).isDisplayed();
    }

    public boolean isCreateAccountFormVisible() {
        return driver.findElement(submitAccountBtn).isDisplayed();
    }

    public void createCheckingAccount(String name, String deposit) {
        driver.findElement(accNameInput).sendKeys(name);
        driver.findElement(accDepositInput).sendKeys(deposit);
        driver.findElement(submitAccountBtn).click();
    }

    public void depositFunds(String amount) throws InterruptedException {
        // 1. Click the first account card to open the transaction modal
        driver.findElement(firstAccountCard).click();
        
        // Wait for modal animation
        Thread.sleep(500); 
        
        // 2. Type amount and submit
        driver.findElement(transAmountInput).sendKeys(amount);
        driver.findElement(submitTransBtn).click();
    }
}