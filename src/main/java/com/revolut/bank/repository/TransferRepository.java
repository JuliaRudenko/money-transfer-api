package com.revolut.bank.repository;

import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.exception.NotSameCurrencyException;
import com.revolut.bank.exception.NotSufficientBalanceException;
import com.revolut.bank.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransferRepository extends BaseH2Repository<Transfer> {
    private static final Logger LOG = LoggerFactory.getLogger(TransferRepository.class);

    @Override
    String getTableName() {
        return "transfer";
    }

    @Override
    List<Transfer> mapResultSetToList(ResultSet rs) throws SQLException {
        List<Transfer> transfers = new ArrayList<>();
        while (rs.next()) {
            transfers.add(new Transfer(rs.getLong("id"), rs.getLong("sourceAccountId"),
                    rs.getLong("destinationAccountId"), rs.getBigDecimal("sum"),
                    rs.getString("currency"), rs.getTimestamp("createdAt")));
        }
        return transfers;
    }

    @Override
    Transfer mapResultSetToObject(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Transfer(rs.getLong("id"), rs.getLong("sourceAccountId"),
                    rs.getLong("destinationAccountId"), rs.getBigDecimal("sum"),
                    rs.getString("currency"), rs.getTimestamp("createdAt"));
        } else {
            throw new NoSuchEntityException("Transfer with such id does not exist!");
        }
    }

    @Override
    String getInsertQuery() {
        return "INSERT INTO transfer(sourceAccountId, destinationAccountId, sum, currency) " +
                "VALUES (?, ?, ?, ?)";
    }

    @Override
    void fillInsertPreparedStatement(PreparedStatement ps, Transfer transfer) throws SQLException {
        ps.setLong(1, transfer.getSourceAccountId());
        ps.setLong(2, transfer.getDestinationAccountId());
        ps.setBigDecimal(3, transfer.getSum());
        ps.setString(4, transfer.getCurrency());
    }

    @Override
    String getUpdateQuery(Transfer transfer) {
        String baseQuery = "UPDATE transfer SET";
        if (transfer.getCurrency() != null && transfer.getSum() != null) {
            baseQuery += " currency=?, sum=? ";
        } else if (transfer.getCurrency() != null) {
            baseQuery += " currency=? ";
        } else if (transfer.getSum() != null) {
            baseQuery += " sum=? ";
        } else {
            return "";
        }
        baseQuery += "WHERE id = ?";
        return baseQuery;
    }

    @Override
    void fillUpdatePreparedStatement(PreparedStatement ps, Transfer transfer) throws SQLException {
        if (transfer.getCurrency() != null && transfer.getSum() != null) {
            ps.setString(1, transfer.getCurrency());
            ps.setBigDecimal(2, transfer.getSum());
            ps.setLong(3, transfer.getId());
        } else if (transfer.getCurrency() != null) {
            ps.setString(1, transfer.getCurrency());
            ps.setLong(2, transfer.getId());
        } else if (transfer.getSum() != null) {
            ps.setBigDecimal(1, transfer.getSum());
            ps.setLong(2, transfer.getId());
        }
    }

    public List<Transfer> findAllTransfers() throws SQLException {
        return findAll();
    }

    public Transfer findTransferById(Long id) throws SQLException {
        return findById(id);
    }

    public Long createTransfer(Transfer transfer) throws SQLException {
        Connection conn = getDBConnection();
        try (
                PreparedStatement selectAccount = conn.prepareStatement(
                        "SELECT balance, currency FROM account WHERE id = ?");
                PreparedStatement updateSourceAccount = conn.prepareStatement(
                        "UPDATE account SET balance = (balance - ?) WHERE id = ?");
                PreparedStatement updateDestinationAccount = conn.prepareStatement(
                        "UPDATE account SET balance = (balance + ?) WHERE id = ?");
        ) {
            conn.setAutoCommit(false);

            // Check if source account exist & its balance
            selectAccount.setLong(1, transfer.getSourceAccountId());
            ResultSet sourceAccountRow = selectAccount.executeQuery();
            String sourceCurrency = checkSourceAccountAndGetCurrency(sourceAccountRow, transfer.getSum());

            // Check if destination account exist
            selectAccount.setLong(1, transfer.getDestinationAccountId());
            ResultSet destinationAccountRow = selectAccount.executeQuery();
            String destinationCurrency = checkDestinationAccountAndGetCurrency(destinationAccountRow);

            // Check that source currency == destination currency = transfer currency
            if (!sourceCurrency.equals(destinationCurrency) || !sourceCurrency.equals(transfer.getCurrency())) {
                throw new NotSameCurrencyException("Source account, destination account and transfer " +
                        "should all have same currency for making transfer!");
            }

            // Transfer money from source account
            updateSourceAccount.setBigDecimal(1, transfer.getSum());
            updateSourceAccount.setLong(2, transfer.getSourceAccountId());
            updateSourceAccount.executeUpdate();

            // Add money to destination account
            updateDestinationAccount.setBigDecimal(1, transfer.getSum());
            updateDestinationAccount.setLong(2, transfer.getDestinationAccountId());
            updateDestinationAccount.executeUpdate();

            // Add transfer row
            Long id = create(transfer);

            conn.commit();
            return id;
        } catch (Exception e) {
            conn.rollback();
            LOG.warn("Can not execute transfer in H2 db! ", e.getMessage());
            throw e;
        } finally {
            conn.close();
        }
    }

    private String checkSourceAccountAndGetCurrency(ResultSet rs, BigDecimal transferSum) throws SQLException {
        if (rs.next()) {
            if (rs.getBigDecimal("balance").compareTo(transferSum) < 0) {
                throw new NotSufficientBalanceException("Source account has not sufficient money for transfer!");
            }
            return rs.getString("currency");
        } else {
            throw new NoSuchEntityException("Source account does not exist!");
        }
    }

    private String checkDestinationAccountAndGetCurrency(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            throw new NoSuchEntityException("Destination account does not exist!");
        }
        return rs.getString("currency");
    }

    public void updateTransfer(Transfer transfer) throws SQLException {
        update(transfer);
    }

    public void deleteTransfer(Long id) throws SQLException {
        delete(id);
    }
}
