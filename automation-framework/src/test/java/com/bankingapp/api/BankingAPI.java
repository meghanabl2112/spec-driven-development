package com.bankingapp.api;

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

    @Test
    public void testOverdraftProtectionReturns422() {
        // 1. Create an account with only $100
        String accountBody = "{\"customerName\":\"Overdraft User\",\"type\":\"Checking\",\"initialDeposit\":100}";
        String accountId = given()
                .header("Content-Type", "application/json")
                .body(accountBody)
                .when().post("/api/accounts")
                .jsonPath().getString("id");

        // 2. Try to withdraw $500 (which should legally fail)
        String transBody = "{\"type\":\"Withdrawal\",\"amount\":500}";
        given()
            .header("Content-Type", "application/json")
            .body(transBody)
            .when().post("/api/accounts/" + accountId + "/transactions")
            .then()
            .statusCode(422); // Asserts the server blocks the overdraft!
    }

    @Test
    public void testSavingsBucketOnCheckingAccountReturns400() {
        // 1. Create a standard Checking account
        String accountBody = "{\"customerName\":\"Checking User\",\"type\":\"Checking\",\"initialDeposit\":1000}";
        String accountId = given()
                .header("Content-Type", "application/json")
                .body(accountBody)
                .when().post("/api/accounts")
                .jsonPath().getString("id");

        // 2. Try to add a Savings Bucket to a Checking Account (which should legally fail)
        String bucketBody = "{\"name\":\"Travel\",\"target\":500}";
        given()
            .header("Content-Type", "application/json")
            .body(bucketBody)
            .when().post("/api/accounts/" + accountId + "/buckets")
            .then()
            .statusCode(400); // Asserts the server blocks invalid logic!
    }
}