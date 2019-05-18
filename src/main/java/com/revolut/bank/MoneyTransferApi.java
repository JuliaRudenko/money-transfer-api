package com.revolut.bank;

import com.revolut.bank.controller.UserController;

import static spark.Spark.get;

public class MoneyTransferApi {
    public static void main(String[] arg) {
        UserController userController = new UserController();

        get("/users", userController.getUsers());
    }
}
