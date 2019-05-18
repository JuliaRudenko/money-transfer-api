package com.revolut.bank;

import com.revolut.bank.controller.AccountController;
import com.revolut.bank.controller.UserController;

import java.sql.SQLException;

import static spark.Spark.*;

public class MoneyTransferApi {
    public static void main(String[] arg) throws SQLException {
        UserController userController = new UserController();
        AccountController accountController = new AccountController();

        get("/users", userController.findAllUsers());
        get("/users/:id", userController.findUserById());
        post("/users", userController.createUser());
        // update userName and/or email
        put("/users", userController.updateUser());
        delete("/users/:id", userController.deleteUser());

        get("/accounts", accountController.findAllAccounts());
        get("/accounts/:id", accountController.findAccountById());
        post("/accounts", accountController.createAccount());
        // update currency and/or balance
        put("/accounts", accountController.updateAccount());
        delete("/accounts/:id", accountController.deleteAccount());
    }
}
