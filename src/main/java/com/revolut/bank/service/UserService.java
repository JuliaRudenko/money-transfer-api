package com.revolut.bank.service;

import com.revolut.bank.model.User;
import com.revolut.bank.repository.UserRepository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService() throws SQLException {
        this.userRepository = new UserRepository();
    }

    public List<User> findAllUsers() throws SQLException {
        return userRepository.findAllUsers();
    }

    public User findUserById(Long id) throws SQLException {
        return userRepository.findUserById(id);
    }

    public void createUser(User user) throws SQLException {
        Long id = userRepository.createUser(user);
        user.setCreatedAt(new Date());
        user.setId(id);
    }

    public User updateUser(User user) throws SQLException {
        userRepository.updateUser(user);
        return userRepository.findUserById(user.getId());
    }

    public void deleteUser(Long id) throws SQLException {
        userRepository.deleteUser(id);
    }
}
