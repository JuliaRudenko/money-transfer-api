package com.revolut.bank.service;

import com.revolut.bank.model.User;
import com.revolut.bank.repository.UserRespository;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserRespository userRespository;

    public UserService() throws SQLException {
        this.userRespository = new UserRespository();
    }

    public UserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    public List<User> findAllUsers() throws SQLException {
        return userRespository.findAllUsers();
    }

    public User findUserById(Long id) throws SQLException {
        return userRespository.findUserById(id);
    }
}
