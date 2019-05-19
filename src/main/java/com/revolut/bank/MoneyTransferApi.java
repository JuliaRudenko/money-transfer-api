package com.revolut.bank;

import com.revolut.bank.controller.AccountController;
import com.revolut.bank.controller.TransferController;
import com.revolut.bank.controller.UserController;

import java.sql.SQLException;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

public class MoneyTransferApi {
    public static void main(String[] arg) throws SQLException {
        UserController userController = new UserController();
        AccountController accountController = new AccountController();
        TransferController transferController = new TransferController();

        get("/users", userController.findAllUsers());
        get("/users/:id", userController.findUserById());
        post("/users", userController.createUser());
        // update userName and/or email only!
        put("/users", userController.updateUser());
        delete("/users/:id", userController.deleteUser());

        get("/accounts", accountController.findAllAccounts());
        get("/accounts/:id", accountController.findAccountById());
        post("/accounts", accountController.createAccount());
        // update currency and/or balance only!
        put("/accounts", accountController.updateAccount());
        delete("/accounts/:id", accountController.deleteAccount());

        get("/transfers", transferController.findAllTransfers());
        get("/transfers/:id", transferController.findTransferById());
        // Transfer money from source Account to destination Account
        post("/transfers", transferController.createTransfer());
        // update currency and/or sum only!
        put("/transfers", transferController.updateTransfer());
        delete("/transfers/:id", transferController.deleteTransfer());
    }
}
