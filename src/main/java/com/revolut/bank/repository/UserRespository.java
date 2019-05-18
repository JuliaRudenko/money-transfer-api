package com.revolut.bank.repository;

import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRespository extends BaseH2Repository<User> {

    // filling whole db data here only once
    public UserRespository() throws SQLException {
        fillDbWithData();
    }

    @Override
    String getTableName() {
        return "user";
    }

    @Override
    List<User> mapResultSetToList(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(new User(rs.getLong("id"), rs.getString("userName"),
                    rs.getString("email"), rs.getDate("createdAt")));
        }
        return users;
    }

    @Override
    User mapResultSetToObject(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new User(rs.getLong("id"), rs.getString("userName"),
                    rs.getString("email"), rs.getDate("createdAt"));
        } else {
            throw new NoSuchEntityException("User with such id does not exist!");
        }
    }

    public List<User> findAllUsers() throws SQLException {
        return findAll();
    }

    public User findUserById(Long id) throws SQLException {
        return findById(id);
    }
}
