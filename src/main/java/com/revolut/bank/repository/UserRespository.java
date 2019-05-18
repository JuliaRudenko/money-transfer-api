package com.revolut.bank.repository;

import com.revolut.bank.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRespository extends BaseH2Repository<User> {

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

    public List<User> getUsers() throws SQLException {
        return findAll();
    }
}
