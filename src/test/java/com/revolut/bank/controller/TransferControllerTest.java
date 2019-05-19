package com.revolut.bank.controller;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class TransferControllerTest extends BaseRestAssuredTest {

    @Test
    public void checkSuccessCodeForTransfers() {
        given().when().get("/transfers").then().statusCode(200);
    }

    @Test
    public void checkSuccessFindTransfer() {
        given().when().get("/transfers/1").then()
                .statusCode(200)
                .body("data.sourceAccountId", equalTo(1))
                .body("data.destinationAccountId", equalTo(2))
                .body("data.sum", equalTo(500))
                .body("data.currency", equalTo("UAH"));
    }

    @Test
    public void checkNoContentForFindTransfer() {
        given().when().get("/transfers/10").then().statusCode(204);
    }

    @Test
    public void checkBadRequestForFindTransfer() {
        given().when().get("/transfers/test").then().statusCode(400);
    }

    @Test
    public void checkSuccessCreateTransfer() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sourceAccountId", 1);
        transfer.put("destinationAccountId", 2);
        transfer.put("sum", 500);
        transfer.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(200)
                .body("data.sourceAccountId", equalTo(1))
                .body("data.destinationAccountId", equalTo(2))
                .body("data.sum", equalTo(500))
                .body("data.currency", equalTo("UAH"))
                .body("data.id", equalTo(4));
    }

    @Test
    public void checkCreateTransferNullSourceAccountId() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("destinationAccountId", 2);
        transfer.put("sum", 500);
        transfer.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateTransferNullDestinationAccountId() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sourceAccountId", 1);
        transfer.put("sum", 500);
        transfer.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateTransferNullSum() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sourceAccountId", 1);
        transfer.put("destinationAccountId", 2);
        transfer.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateTransferNullCurrency() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sourceAccountId", 1);
        transfer.put("destinationAccountId", 2);
        transfer.put("sum", 500);

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateTransferSourceAccountNotExist() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sourceAccountId", 10);
        transfer.put("destinationAccountId", 2);
        transfer.put("sum", 500);
        transfer.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateTransferDestinationAccountNotExist() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sourceAccountId", 1);
        transfer.put("destinationAccountId", 10);
        transfer.put("sum", 500);
        transfer.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateTransferNotEnoughMoney() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sourceAccountId", 10);
        transfer.put("destinationAccountId", 2);
        transfer.put("sum", 500000);
        transfer.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateTransferDifferentCurrencies() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sourceAccountId", 10);
        transfer.put("destinationAccountId", 2);
        transfer.put("sum", 500);
        transfer.put("currency", "GBP");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().post("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkSuccessUpdateTransfer() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("sum", 1000);
        transfer.put("currency", "GBP");
        transfer.put("id", 1);

        given()
                .contentType("application/json")
                .body(transfer)
                .when().put("/transfers").then()
                .statusCode(200)
                .body("data.sum", equalTo(1000))
                .body("data.currency", equalTo("GBP"))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkSuccessUpdateTransferWithSumOnly() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("id", 1);
        transfer.put("sum", 200);

        given()
                .contentType("application/json")
                .body(transfer)
                .when().put("/transfers").then()
                .statusCode(200)
                .body("data.sum", equalTo(200))
                .body("data.currency", equalTo("UAH"))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkSuccessUpdateTransferWithCurrencyOnly() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("id", 1);
        transfer.put("currency", "GBP");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().put("/transfers").then()
                .statusCode(200)
                .body("data.sum", equalTo(500))
                .body("data.currency", equalTo("GBP"))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkBadRequestUpdateTransferWithIdOnly() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("id", 1L);

        given()
                .contentType("application/json")
                .body(transfer)
                .when().put("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkBadRequestUpdateTransferWithoutId() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("currency", "GBP");

        given()
                .contentType("application/json")
                .body(transfer)
                .when().put("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkBadRequestUpdateTransferWithSourceAccountId() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("id", 1);
        transfer.put("sourceAccountId", 3);

        given()
                .contentType("application/json")
                .body(transfer)
                .when().put("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkBadRequestUpdateTransferWithDestinationAccountId() {
        Map<String, Object> transfer = new HashMap<>();
        transfer.put("id", 1);
        transfer.put("destinationAccountId", 3);

        given()
                .contentType("application/json")
                .body(transfer)
                .when().put("/transfers").then()
                .statusCode(400);
    }

    @Test
    public void checkSuccessDeleteTransfer() {
        given()
                .when().delete("/transfers/3").then()
                .statusCode(200);
    }

    @Test
    public void checkBadRequestDeleteTransfer() {
        given()
                .when().delete("/transfers/test").then()
                .statusCode(400);
    }
}
