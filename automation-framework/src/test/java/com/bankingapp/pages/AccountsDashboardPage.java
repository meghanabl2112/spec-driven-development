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
}
