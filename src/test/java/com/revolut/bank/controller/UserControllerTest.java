package com.revolut.bank.controller;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserControllerTest extends BaseRestAssuredTest {

    @Test
    public void checkSuccessCodeForUsers() {
        given().when().get("/users").then().statusCode(200);
    }

    @Test
    public void checkSuccessFindUser() {
        given().when().get("/users/1").then()
                .statusCode(200)
                .body("data.userName", equalTo("Eddard Stark"))
                .body("data.email", equalTo("eddard@gmail.com"));
    }

    @Test
    public void checkNoContentForFindUser() {
        given().when().get("/users/10").then().statusCode(204);
    }

    @Test
    public void checkBadRequestForFindUser() {
        given().when().get("/users/test").then().statusCode(400);
    }

    @Test
    public void checkSuccessCreateUser() {
        Map<String, String> user = new HashMap<>();
        String userName = "Jaime Lannister";
        String email = "jaime@gmail.com";
        user.put("userName", userName);
        user.put("email", email);

        given()
                .contentType("application/json")
                .body(user)
                .when().post("/users").then()
                .statusCode(200)
                .body("data.userName", equalTo(userName))
                .body("data.email", equalTo(email))
                .body("data.id", equalTo(4));
    }

    @Test
    public void checkCreateUserNullEmail() {
        Map<String, String> user = new HashMap<>();
        String userName = "Jaime Lannister";
        user.put("userName", userName);

        given()
                .contentType("application/json")
                .body(user)
                .when().post("/users").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateUserNullUserName() {
        Map<String, String> user = new HashMap<>();
        String email = "jaime@gmail.com";
        user.put("email", email);

        given()
                .contentType("application/json")
                .body(user)
                .when().post("/users").then()
                .statusCode(400);
    }

    @Test
    public void checkCreateUserNullUserNameAndEmail() {
        Map<String, String> user = new HashMap<>();

        given()
                .contentType("application/json")
                .body(user)
                .when().post("/users").then()
                .statusCode(400);
    }

    @Test
    public void checkSuccessUpdateUser() {
        Map<String, Object> user = new HashMap<>();
        String userName = "Jaime Lannister";
        String email = "jaime@gmail.com";
        user.put("userName", userName);
        user.put("email", email);
        user.put("id", 1L);

        given()
                .contentType("application/json")
                .body(user)
                .when().put("/users").then()
                .statusCode(200)
                .body("data.userName", equalTo(userName))
                .body("data.email", equalTo(email))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkSuccessUpdateUserWithUserNameOnly() {
        Map<String, Object> user = new HashMap<>();
        String userName = "Jaime Lannister";
        String email = "eddard@gmail.com";
        user.put("userName", userName);
        user.put("id", 1L);

        given()
                .contentType("application/json")
                .body(user)
                .when().put("/users").then()
                .statusCode(200)
                .body("data.userName", equalTo(userName))
                .body("data.email", equalTo(email))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkSuccessUpdateUserWithEmailOnly() {
        Map<String, Object> user = new HashMap<>();
        String userName = "Eddard Stark";
        String email = "eddard@gmail.com";
        user.put("email", email);
        user.put("id", 1L);

        given()
                .contentType("application/json")
                .body(user)
                .when().put("/users").then()
                .statusCode(200)
                .body("data.userName", equalTo(userName))
                .body("data.email", equalTo(email))
                .body("data.id", equalTo(1));
    }

    @Test
    public void checkBadRequestUpdateUserWithIdOnly() {
        Map<String, Object> user = new HashMap<>();
        user.put("id", 1L);

        given()
                .contentType("application/json")
                .body(user)
                .when().put("/users").then()
                .statusCode(400);
    }

    @Test
    public void checkBadRequestUpdateUserWithoutId() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", "jaime@hmail.com");

        given()
                .contentType("application/json")
                .body(user)
                .when().put("/users").then()
                .statusCode(400);
    }

    @Test
    public void checkSuccessDeleteUser() {
        given()
                .when().delete("/users/3").then()
                .statusCode(200);
    }

    @Test
    public void checkBadRequestDeleteUser() {
        given()
                .when().delete("/users/test").then()
                .statusCode(400);
    }
}
