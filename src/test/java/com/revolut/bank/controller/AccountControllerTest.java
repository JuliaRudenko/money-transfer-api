package com.revolut.bank.controller;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class AccountControllerTest extends BaseRestAssuredTest {

    @Test
    public void checkSuccessCodeForAccounts() {
        given().when().get("/accounts").then().statusCode(200);
    }

    @Test
    public void checkSuccessFindAccount() {
        given().when().get("/accounts/1").then()
                .statusCode(200)
                .body("data.userId", equalTo(1))
                .body("data.balance", equalTo(100500))
                .body("data.currency", equalTo("UAH"));
    }

    @Test
    public void checkNoContentForFindAccount() {
        given().when().get("/accounts/10").then().statusCode(204);
    }

    @Test
    public void checkBadRequestForFindAccount() {
        given().when().get("/accounts/test").then().statusCode(400);
    }

    @Test
    public void checkSuccessCreateAccount() {
        Map<String, Object> account = new HashMap<>();
        account.put("userId", 1);
        account.put("balance", 200);
        account.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(account)
                .when().post("/accounts").then()
                .statusCode(200)
                .body("data.userId", equalTo(1))
                .body("data.balance", equalTo(200))
                .body("data.currency", equalTo("UAH"))
                .body("data.id", equalTo(4));
    }

    @Test
    public void checkCreateAccountNullUserId() {
        Map<String, Object> account = new HashMap<>();
        account.put("balance", 200);
        account.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(account)
                .when().post("/accounts").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateAccountNullBalance() {
        Map<String, Object> account = new HashMap<>();
        account.put("userId", 1);
        account.put("currency", "UAH");

        given()
                .contentType("application/json")
                .body(account)
                .when().post("/accounts").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateAccountNullCurrency() {
        Map<String, Object> account = new HashMap<>();
        account.put("userId", 1);
        account.put("balance", 200);

        given()
                .contentType("application/json")
                .body(account)
                .when().post("/accounts").then()
                .statusCode(400);
    }

    @Test
    public void checkSuccessUpdateAccount() {
        Map<String, Object> account = new HashMap<>();
        account.put("id", 1);
        account.put("balance", 200);
        account.put("currency", "GBP");

        given()
                .contentType("application/json")
                .body(account)
                .when().put("/accounts").then()
                .statusCode(200)
                .body("data.balance", equalTo(200))
                .body("data.currency", equalTo("GBP"))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkSuccessUpdateAccountWithBalanceOnly() {
        Map<String, Object> account = new HashMap<>();
        account.put("id", 1);
        account.put("balance", 200);

        given()
                .contentType("application/json")
                .body(account)
                .when().put("/accounts").then()
                .statusCode(200)
                .body("data.balance", equalTo(200))
                .body("data.currency", equalTo("UAH"))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkSuccessUpdateAccountWithCurrencyOnly() {
        Map<String, Object> account = new HashMap<>();
        account.put("id", 1);
        account.put("currency", "GBP");

        given()
                .contentType("application/json")
                .body(account)
                .when().put("/accounts").then()
                .statusCode(200)
                .body("data.balance", equalTo(100500))
                .body("data.currency", equalTo("GBP"))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkBadRequestUpdateAccountWithIdOnly() {
        Map<String, Object> account = new HashMap<>();
        account.put("id", 1L);

        given()
                .contentType("application/json")
                .body(account)
                .when().put("/accounts").then()
                .statusCode(400);
    }

    @Test
    public void checkBadRequestUpdateAccountWithoutId() {
        Map<String, Object> account = new HashMap<>();
        account.put("currency", "GBP");

        given()
                .contentType("application/json")
                .body(account)
                .when().put("/accounts").then()
                .statusCode(400);
    }

    @Test
    public void checkBadRequestUpdateAccountWithUserId() {
        Map<String, Object> account = new HashMap<>();
        account.put("userId", 1);
        account.put("id", 1);

        given()
                .contentType("application/json")
                .body(account)
                .when().put("/accounts").then()
                .statusCode(400);
    }

    @Test
    public void checkSuccessDeleteAccount() {
        given()
                .when().delete("/accounts/3").then()
                .statusCode(200);
    }

    @Test
    public void checkBadRequestDeleteAccount() {
        given()
                .when().delete("/accounts/test").then()
                .statusCode(400);
    }
}
