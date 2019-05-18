package com.revolut.bank.controller;

import com.google.gson.Gson;
import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.model.Account;
import com.revolut.bank.service.AccountService;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.util.List;

public class AccountController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private AccountService accountService;

    public AccountController() {
        this.accountService = new AccountService();
    }

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public Route findAllAccounts() {
        return (request, response) -> {
            response.type("application/json");
            try {
                List<Account> users = accountService.findAllAccounts();
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(users)));
            } catch (Exception e) {
                LOG.warn("Exception in findAllAccounts() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route findAccountById() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Long id = Long.parseLong(request.params(":id"));
                Account user = accountService.findAccountById(id);
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(user)));
            } catch (NoSuchEntityException e) {
                response.status(HttpStatus.NO_CONTENT_204);
                return "";
            } catch (Exception e) {
                LOG.warn("Exception in findAccountById() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }
}
