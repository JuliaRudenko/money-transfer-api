package com.revolut.bank;

import com.revolut.bank.controller.AccountController;
import com.revolut.bank.controller.UserController;

import java.sql.SQLException;

import static spark.Spark.get;

public class MoneyTransferApi {
    public static void main(String[] arg) throws SQLException {
        UserController userController = new UserController();
        AccountController accountController = new AccountController();

        get("/users", userController.findAllUsers());
        get("/users/:id", userController.findUserById());

        get("/accounts", accountController.findAllAccounts());
        get("/accounts/:id", accountController.findAccountById());
    }
}
