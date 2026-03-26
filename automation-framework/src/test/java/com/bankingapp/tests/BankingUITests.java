package test.java.com.bankingapp.tests;

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
}