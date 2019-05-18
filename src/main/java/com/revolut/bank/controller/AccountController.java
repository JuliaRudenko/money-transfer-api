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
    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

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
                List<Account> accounts = accountService.findAllAccounts();
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(accounts)));
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
                Account account = accountService.findAccountById(id);
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(account)));
            } catch (NumberFormatException e) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return new Gson().toJson(new StandardResponse("Id should has integer type!"));
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

    public Route createAccount() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Account account = new Gson().fromJson(request.body(), Account.class);
                if (account.getUserId() == null || account.getBalance() == null || account.getCurrency() == null) {
                    response.status(HttpStatus.BAD_REQUEST_400);
                    return new Gson().toJson(new StandardResponse("Fill userId, balance, currency for creating account!"));
                }
                accountService.createAccount(account);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(account)));
            } catch (Exception e) {
                LOG.warn("Exception in createAccount() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route updateAccount() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Account account = new Gson().fromJson(request.body(), Account.class);
                if (account.getUserId() != null || account.getBalance() == null && account.getCurrency() == null
                        || account.getId() == null) {
                    response.status(HttpStatus.BAD_REQUEST_400);
                    return new Gson().toJson(new StandardResponse("Add id and (balance and/or currency) to update account! " +
                            "Do not add userId - we can't change user for account!"));
                }
                Account updatedAccount = accountService.updateAccount(account);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(updatedAccount)));
            } catch (Exception e) {
                LOG.warn("Exception in updateAccount() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route deleteAccount() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Long id = Long.parseLong(request.params(":id"));
                accountService.deleteAccount(id);
                return "";
            } catch (NumberFormatException e) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return new Gson().toJson(new StandardResponse("Id should has integer type!"));
            } catch (Exception e) {
                LOG.warn("Exception in deleteAccount() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }
}
