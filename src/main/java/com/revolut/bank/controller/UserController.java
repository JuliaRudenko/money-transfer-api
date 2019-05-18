package com.revolut.bank.controller;

import com.google.gson.Gson;
import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.model.User;
import com.revolut.bank.service.UserService;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.sql.SQLException;
import java.util.List;

public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController() throws SQLException {
        this.userService = new UserService();
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Route findAllUsers() {
        return (request, response) -> {
            response.type("application/json");
            try {
                List<User> users = userService.findAllUsers();
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(users)));
            } catch (Exception e) {
                LOG.warn("Exception in findAllUsers() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route findUserById() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Long id = Long.parseLong(request.params(":id"));
                User user = userService.findUserById(id);
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(user)));
            } catch (NumberFormatException e) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return new Gson().toJson(new StandardResponse("Id should has integer type!"));
            } catch (NoSuchEntityException e) {
                response.status(HttpStatus.NO_CONTENT_204);
                return "";
            } catch (Exception e) {
                LOG.warn("Exception in findUserById() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route createUser() {
        return (request, response) -> {
            response.type("application/json");
            try {
                User user = new Gson().fromJson(request.body(), User.class);
                if (user.getUserName() == null || user.getEmail() == null) {
                    response.status(HttpStatus.BAD_REQUEST_400);
                    return new Gson().toJson(new StandardResponse("Fill userName, email for creating user!"));
                }
                userService.createUser(user);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(user)));
            } catch (Exception e) {
                LOG.warn("Exception in createUser() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route updateUser() {
        return (request, response) -> {
            response.type("application/json");
            try {
                User user = new Gson().fromJson(request.body(), User.class);
                if (user.getUserName() == null && user.getEmail() == null || user.getId() == null) {
                    response.status(HttpStatus.BAD_REQUEST_400);
                    return new Gson().toJson(new StandardResponse("Add id and (userName and/or email) to update user!"));
                }
                User updatedUser = userService.updateUser(user);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(updatedUser)));
            } catch (Exception e) {
                LOG.warn("Exception in updateUser() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route deleteUser() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Long id = Long.parseLong(request.params(":id"));
                userService.deleteUser(id);
                return "";
            } catch (NumberFormatException e) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return new Gson().toJson(new StandardResponse("Id should has integer type!"));
            } catch (Exception e) {
                LOG.warn("Exception in deleteUser() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }
}
