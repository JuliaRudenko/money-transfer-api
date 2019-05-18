package com.revolut.bank.repository;

import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.model.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository extends BaseH2Repository<Account> {

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
                    rs.getString("currency"), rs.getTimestamp("createdAt")));
        }
        return users;
    }

    @Override
    Account mapResultSetToObject(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Account(rs.getLong("id"), rs.getLong("userId"),
                    rs.getBigDecimal("balance"),
                    rs.getString("currency"), rs.getTimestamp("createdAt"));
        } else {
            throw new NoSuchEntityException("Account with such id does not exist!");
        }
    }

    @Override
    String getInsertQuery() {
        return "INSERT INTO account(userId, balance, currency) VALUES (?, ?, ?)";
    }

    @Override
    void fillInsertPreparedStatement(PreparedStatement ps, Account entity) throws SQLException {
        ps.setLong(1, entity.getUserId());
        ps.setBigDecimal(2, entity.getBalance());
        ps.setString(3, entity.getCurrency());
    }

    @Override
    String getUpdateQuery(Account account) {
        String baseQuery = "UPDATE account SET";
        if (account.getCurrency() != null && account.getBalance() != null) {
            baseQuery += " currency=?, balance=? ";
        } else if (account.getCurrency() != null) {
            baseQuery += " currency=? ";
        } else if (account.getBalance() != null) {
            baseQuery += " balance=? ";
        } else {
            return "";
        }
        baseQuery += "WHERE id = ?";
        return baseQuery;
    }

    @Override
    void fillUpdatePreparedStatement(PreparedStatement ps, Account account) throws SQLException {
        if (account.getCurrency() != null && account.getBalance() != null) {
            ps.setString(1, account.getCurrency());
            ps.setBigDecimal(2, account.getBalance());
            ps.setLong(3, account.getId());
        } else if (account.getCurrency() != null) {
            ps.setString(1, account.getCurrency());
            ps.setLong(2, account.getId());
        } else if (account.getBalance() != null) {
            ps.setBigDecimal(1, account.getBalance());
            ps.setLong(2, account.getId());
        }
    }

    public List<Account> findAllAccounts() throws SQLException {
        return findAll();
    }

    public Account findAccountById(Long id) throws SQLException {
        return findById(id);
    }

    public Long createAccount(Account account) throws SQLException {
        return create(account);
    }

    public void updateAccount(Account account) throws SQLException {
        update(account);
    }

    public void deleteAccount(Long id) throws SQLException {
        delete(id);
    }
}
