package com.revolut.bank.service;

import com.revolut.bank.model.User;
import com.revolut.bank.repository.UserRespository;

import java.sql.SQLException;
import java.util.Date;
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

    public void createUser(User user) throws SQLException {
        Long id = userRespository.createUser(user);
        user.setCreatedAt(new Date());
        user.setId(id);
    }

    public User updateUser(User user) throws SQLException {
        userRespository.updateUser(user);
        return userRespository.findUserById(user.getId());
    }

    public void deleteUser(Long id) throws SQLException {
        userRespository.deleteUser(id);
    }
}
