package com.revolut.bank.repository;

import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.exception.NotSameCurrencyException;
import com.revolut.bank.exception.NotSufficientBalanceException;
import com.revolut.bank.model.Account;
import com.revolut.bank.model.Transfer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TransferRepositoryTest {
    private static TransferRepository transferRepository;
    private static AccountRepository accountRepository;

    @BeforeClass
    public static void init() {
        transferRepository = new TransferRepository();
        accountRepository = new AccountRepository();
    }

    @Before
    public void refreshDbData() throws SQLException {
        transferRepository.fillDbWithData();
    }

    @Test
    public void checkFindAllTransfers() throws SQLException {
        List<Transfer> transfers = transferRepository.findAllTransfers();
        assertEquals(3, transfers.size());
    }

    @Test
    public void checkFindTransferById() throws SQLException {
        Transfer transfer = transferRepository.findTransferById(1L);
        assertEquals(new BigDecimal(500), transfer.getSum());
    }

    @Test(expected = NoSuchEntityException.class)
    public void checkFindNonExistentTransferById() throws SQLException {
        transferRepository.findTransferById(10L);
    }

    @Test
    public void checkCreateTransferSuccess() throws SQLException {
        Transfer transfer = new Transfer(1L, 2L, new BigDecimal(500.0), "UAH");
        Long id = transferRepository.createTransfer(transfer);

        // check that transfer row created
        assertEquals(new Long(4L), id);
        // check that source account got less balance
        Account sourceAccount = accountRepository.findAccountById(transfer.getSourceAccountId());
        assertEquals(new BigDecimal(100000), sourceAccount.getBalance());
        // check that destination account got more balance
        Account destinationAccount = accountRepository.findAccountById(transfer.getDestinationAccountId());
        assertEquals(new BigDecimal(2500), destinationAccount.getBalance());
    }

    @Test(expected = NoSuchEntityException.class)
    public void checkCreateTransferWhenSourceAccountNotExist() throws SQLException {
        Transfer transfer = new Transfer(10L, 2L, new BigDecimal(12345.0), "UAH");
        transferRepository.createTransfer(transfer);
    }

    @Test(expected = NoSuchEntityException.class)
    public void checkCreateTransferWhenDestinationAccountNotExist() throws SQLException {
        Transfer transfer = new Transfer(1L, 10L, new BigDecimal(12345.0), "UAH");
        transferRepository.createTransfer(transfer);
    }

    @Test(expected = NotSufficientBalanceException.class)
    public void checkCreateTransferWhenNotEnoughMoney() throws SQLException {
        Transfer transfer = new Transfer(1L, 2L, new BigDecimal(100600.0), "UAH");
        transferRepository.createTransfer(transfer);
    }

    @Test(expected = NotSameCurrencyException.class)
    public void checkCreateTransferWhenDifferentCurrencies() throws SQLException {
        Transfer transfer = new Transfer(1L, 2L, new BigDecimal(100000), "GBP");
        transferRepository.createTransfer(transfer);
    }

    @Test
    public void checkUpdateSumAndCurrency() throws SQLException {
        BigDecimal sum = new BigDecimal(190.60);
        String currency = "UAH";
        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setSum(sum);
        transfer.setCurrency(currency);

        transferRepository.updateTransfer(transfer);
        // already tested by another tests
        Transfer transferFromDb = transferRepository.findTransferById(1L);

        assertEquals(sum, transferFromDb.getSum());
        assertEquals(currency, transferFromDb.getCurrency());
    }

    @Test
    public void checkUpdateSum() throws SQLException {
        BigDecimal sum = new BigDecimal(190.60);
        String currency = "UAH";
        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setSum(sum);

        transferRepository.updateTransfer(transfer);
        // already tested by another tests
        Transfer transferFromDb = transferRepository.findTransferById(1L);

        assertEquals(sum, transferFromDb.getSum());
        assertEquals(currency, transferFromDb.getCurrency());
    }

    @Test
    public void checkUpdateCurrency() throws SQLException {
        BigDecimal balance = new BigDecimal(500);
        String currency = "GBP";
        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setCurrency(currency);

        transferRepository.updateTransfer(transfer);
        // already tested by another tests
        Transfer transferFromDb = transferRepository.findTransferById(1L);

        assertEquals(balance, transferFromDb.getSum());
        assertEquals(currency, transferFromDb.getCurrency());
    }

    @Test
    public void checkWillNotUpdateCreatedAt() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date updateDate = format.parse("2020-10-10 11:00:00");
        Date expectedDate = format.parse("2019-05-17 17:00:00");
        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setCreatedAt(updateDate);

        transferRepository.updateTransfer(transfer);
        // already tested by another tests
        Transfer transferFromDb = transferRepository.findTransferById(1L);

        assertEquals(expectedDate, transferFromDb.getCreatedAt());
    }

    @Test
    public void checkWillNotUpdateSourceAccountId() throws SQLException {
        Long expectedSourceAccountId = 1L;
        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setSourceAccountId(2L);

        transferRepository.updateTransfer(transfer);
        // already tested by another tests
        Transfer transferFromDb = transferRepository.findTransferById(1L);

        assertEquals(expectedSourceAccountId, transferFromDb.getSourceAccountId());
    }

    @Test
    public void checkWillNotUpdateDestinationAccountId() throws SQLException {
        Long expectedDestinationAccountId = 2L;
        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setSourceAccountId(2L);

        transferRepository.updateTransfer(transfer);
        // already tested by another tests
        Transfer transferFromDb = transferRepository.findTransferById(1L);

        assertEquals(expectedDestinationAccountId, transferFromDb.getDestinationAccountId());
    }

    @Test(expected = NoSuchEntityException.class)
    public void checkThatNoTransferAfterDelete() throws SQLException {
        transferRepository.deleteTransfer(3L);
        transferRepository.findTransferById(3L);
    }

    @Test
    public void checkThatTwoTransfersLeftAfterDelete() throws SQLException {
        transferRepository.deleteTransfer(3L);
        List<Transfer> transfers = transferRepository.findAllTransfers();
        assertEquals(2, transfers.size());
    }
}
