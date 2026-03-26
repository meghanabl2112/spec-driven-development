package test.java.com.bankingapp.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BankingAPI {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:3000";
    }

    @Test
    public void testGetAccounts() {
        given()
            .when().get("/api/accounts")
            .then().statusCode(200);
    }

    @Test
    public void testDepositTransaction() {
        // First, create an account
        String accountBody = "{\"customerName\":\"Test User\",\"type\":\"Checking\",\"initialDeposit\":1000}";
        String accountId = given()
                .header("Content-Type", "application/json")
                .body(accountBody)
                .when().post("/api/accounts")
                .jsonPath().getString("id");

        // Then, perform a deposit
        String transBody = "{\"type\":\"Deposit\",\"amount\":500}";
        given()
            .header("Content-Type", "application/json")
            .body(transBody)
            .when().post("/api/accounts/" + accountId + "/transactions")
            .then()
            .statusCode(201)
            .body("newBalance", equalTo(1500.0f));
    }

    @Test
    public void testCreateSavingsBucket() {
        // Create savings account
        String accountBody = "{\"customerName\":\"Savings User\",\"type\":\"Savings\",\"initialDeposit\":5000}";
        String accountId = given()
                .header("Content-Type", "application/json")
                .body(accountBody)
                .when().post("/api/accounts")
                .jsonPath().getString("id");

        // Add a bucket
        String bucketBody = "{\"name\":\"Emergency Fun\",\"target\":10000}";
        given()
            .header("Content-Type", "application/json")
            .body(bucketBody)
            .when().post("/api/accounts/" + accountId + "/buckets")
            .then()
            .statusCode(201);
    }
}