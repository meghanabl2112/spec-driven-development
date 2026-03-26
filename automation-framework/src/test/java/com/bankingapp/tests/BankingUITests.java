package com.bankingapp.tests;

import com.bankingapp.pages.AccountsDashboardPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankingUITests {
    private WebDriver driver;
    private AccountsDashboardPage dashboard;

    @BeforeEach
    public void setup() {
        // Ensure you have ChromeDriver installed or use WebDriverManager
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run headless for CI/CD
        driver = new ChromeDriver(options);
        
        // Navigate to the local server you built in Phases 1-3
        driver.get("http://localhost:3000");
        dashboard = new AccountsDashboardPage(driver);
    }

    @Test
    public void testDashboardOverviewLoads() {
        assertTrue(dashboard.isOverviewHeaderVisible(), "Accounts Overview header should be visible.");
    }

    @Test
    public void testCreateAccountFormIsPresent() {
        assertTrue(dashboard.isCreateAccountFormVisible(), "Create Account button should be visible.");
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateAccountFlow() throws InterruptedException {
        // Create an account
        dashboard.createCheckingAccount("Selenium User", "2500");
        
        // Wait a brief moment for the DOM to update via the explicit fetch call
        Thread.sleep(1000); 

        // Verify the account is visible in the page source
        assertTrue(driver.getPageSource().contains("Selenium User"));
        assertTrue(driver.getPageSource().contains("$2500.00"));
    }

   @Test
    public void testDepositFlow() throws InterruptedException {
        // Ensure there is at least one account to click on
        dashboard.createCheckingAccount("Transaction User", "1000");
        Thread.sleep(1000); // let UI update
        
        // Click the card and deposit $500
        dashboard.depositFunds("500");
        
        // Wait for the UI success message to appear
        Thread.sleep(1000); 
        
        // In your real UI, the message appears inside the div "transMessage"
        assertTrue(driver.getPageSource().contains("Successful"));
    }
}