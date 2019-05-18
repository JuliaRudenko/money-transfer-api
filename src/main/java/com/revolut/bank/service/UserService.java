package com.revolut.bank.service;

import com.revolut.bank.model.User;
import com.revolut.bank.repository.UserRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private UserRespository userRespository;

    public UserService() {
        this.userRespository = new UserRespository();
    }

    public UserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    public List<User> getUsers() throws SQLException {
        try {
            return userRespository.getUsers();
        } catch (Exception e) {
            LOG.warn("Exception in getUsers() ", e);
            throw e;
        }
    }
}
