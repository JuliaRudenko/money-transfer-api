package com.revolut.bank.repository;

import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends BaseH2Repository<User> {

    // filling whole db data here only once
    public UserRepository() throws SQLException {
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
                    rs.getString("email"), rs.getTimestamp("createdAt")));
        }
        return users;
    }

    @Override
    User mapResultSetToObject(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new User(rs.getLong("id"), rs.getString("userName"),
                    rs.getString("email"), rs.getTimestamp("createdAt"));
        } else {
            throw new NoSuchEntityException("User with such id does not exist!");
        }
    }

    @Override
    String getInsertQuery() {
        return "INSERT INTO user(userName, email) VALUES (?, ?)";
    }

    @Override
    void fillInsertPreparedStatement(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getUserName());
        ps.setString(2, user.getEmail());
    }

    @Override
    String getUpdateQuery(User user) {
        String baseQuery = "UPDATE user SET";
        if (user.getUserName() != null && user.getEmail() != null) {
            baseQuery += " userName=?, email=? ";
        } else if (user.getUserName() != null) {
            baseQuery += " userName=? ";
        } else if (user.getEmail() != null) {
            baseQuery += " email=? ";
        } else {
            return "";
        }
        baseQuery += "WHERE id = ?";
        return baseQuery;
    }

    @Override
    void fillUpdatePreparedStatement(PreparedStatement ps, User user) throws SQLException {
        if (user.getUserName() != null && user.getEmail() != null) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setLong(3, user.getId());
        } else if (user.getUserName() != null) {
            ps.setString(1, user.getUserName());
            ps.setLong(2, user.getId());
        } else if (user.getEmail() != null) {
            ps.setString(1, user.getEmail());
            ps.setLong(2, user.getId());
        }
    }

    public List<User> findAllUsers() throws SQLException {
        return findAll();
    }

    public User findUserById(Long id) throws SQLException {
        return findById(id);
    }

    public Long createUser(User user) throws SQLException {
        return create(user);
    }

    public void updateUser(User user) throws SQLException {
        update(user);
    }

    public void deleteUser(Long id) throws SQLException {
        delete(id);
    }
}
