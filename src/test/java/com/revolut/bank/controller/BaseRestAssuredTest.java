package com.revolut.bank.controller;

import com.jayway.restassured.RestAssured;
import com.revolut.bank.MoneyTransferApi;
import com.revolut.bank.repository.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.SQLException;

public class BaseRestAssuredTest {
    private static UserRepository userRepository;

    @BeforeClass
    public static void setup() throws SQLException {
        userRepository = new UserRepository();
        RestAssured.port = 4567;
        RestAssured.basePath = "/money-transfer";
        RestAssured.baseURI = "http://localhost";
        MoneyTransferApi.main(new String[]{});
    }

    @Before
    public void refreshDbData() throws SQLException {
        userRepository.fillDbWithData();
    }
}
