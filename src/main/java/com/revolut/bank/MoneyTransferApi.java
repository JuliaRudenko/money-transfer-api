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

        get("/money-transfer/users", userController.findAllUsers());
        get("/money-transfer/users/:id", userController.findUserById());
        post("/money-transfer/users", userController.createUser());
        // update userName and/or email only!
        put("/money-transfer/users", userController.updateUser());
        delete("/money-transfer/users/:id", userController.deleteUser());

        get("/money-transfer/accounts", accountController.findAllAccounts());
        get("/money-transfer/accounts/:id", accountController.findAccountById());
        post("/money-transfer/accounts", accountController.createAccount());
        // update currency and/or balance only!
        put("/money-transfer/accounts", accountController.updateAccount());
        delete("/money-transfer/accounts/:id", accountController.deleteAccount());

        get("/money-transfer/transfers", transferController.findAllTransfers());
        get("/money-transfer/transfers/:id", transferController.findTransferById());
        // Transfer money from source Account to destination Account
        post("/money-transfer/transfers", transferController.createTransfer());
        // update currency and/or sum only!
        put("/money-transfer/transfers", transferController.updateTransfer());
        delete("/money-transfer/transfers/:id", transferController.deleteTransfer());
    }
}
