package com.revolut.bank.controller;

import com.google.gson.Gson;
import com.revolut.bank.model.User;
import com.revolut.bank.service.UserService;
import org.eclipse.jetty.http.HttpStatus;
import spark.Route;

import java.util.List;

public class UserController {
    private UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Route getUsers() {
        return (request, response) -> {
            response.type("application/json");
            try {
                List<User> users = userService.getUsers();
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(users)));
            } catch (Exception e) {
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
            }
        };
    }
}
