package com.revolut.bank.repository;

import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository extends BaseH2Repository<Account> {
    public AccountRepository() {
    }

    @Override
    String getTableName() {
        return "account";
    }

    @Override
    List<Account> mapResultSetToList(ResultSet rs) throws SQLException {
        List<Account> users = new ArrayList<>();
        while (rs.next()) {
            users.add(new Account(rs.getLong("id"), rs.getLong("userId"),
                    rs.getBigDecimal("balance"),
                    rs.getString("currency"), rs.getDate("createdAt")));
        }
        return users;
    }

    @Override
    Account mapResultSetToObject(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Account(rs.getLong("id"), rs.getLong("userId"),
                    rs.getBigDecimal("balance"),
                    rs.getString("currency"), rs.getDate("createdAt"));
        } else {
            throw new NoSuchEntityException("Account with such id does not exist!");
        }
    }

    public List<Account> findAllAccounts() throws SQLException {
        return findAll();
    }

    public Account findAccountById(Long id) throws SQLException {
        return findById(id);
    }
}
