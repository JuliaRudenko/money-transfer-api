package com.revolut.bank.repository;

import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.model.Account;
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

public class AccountRepositoryTest {
    private static AccountRepository accountRepository;

    @BeforeClass
    public static void init() {
        accountRepository = new AccountRepository();
    }

    @Before
    public void refreshDbData() throws SQLException {
        accountRepository.fillDbWithData();
    }

    @Test
    public void checkFindAllAccounts() throws SQLException {
        List<Account> accounts = accountRepository.findAllAccounts();
        assertEquals(3, accounts.size());
    }

    @Test
    public void checkFindAccountById() throws SQLException {
        Account account = accountRepository.findAccountById(1L);
        assertEquals(new BigDecimal(100500), account.getBalance());
    }

    @Test(expected = NoSuchEntityException.class)
    public void checkFindNonExistentAccountById() throws SQLException {
        accountRepository.findAccountById(10L);
    }

    @Test
    public void checkCreateAccount() throws SQLException {
        Account account = new Account(1L, new BigDecimal(12345.0), "UAH");
        Long id = accountRepository.createAccount(account);
        assertEquals(new Long(4L), id);
    }

    @Test
    public void checkUpdateBalanceAndCurrency() throws SQLException {
        BigDecimal balance = new BigDecimal(190.60);
        String currency = "GBP";
        Account account = new Account(balance, currency);
        account.setId(1L);

        accountRepository.updateAccount(account);
        // already tested by another tests
        Account accountFromDb = accountRepository.findAccountById(1L);

        assertEquals(balance, accountFromDb.getBalance());
        assertEquals(currency, accountFromDb.getCurrency());
    }

    @Test
    public void checkUpdateBalance() throws SQLException {
        BigDecimal balance = new BigDecimal(190.60);
        String currency = "UAH";
        Account account = new Account();
        account.setId(1L);
        account.setBalance(balance);

        accountRepository.updateAccount(account);
        // already tested by another tests
        Account accountFromDb = accountRepository.findAccountById(1L);

        assertEquals(balance, accountFromDb.getBalance());
        assertEquals(currency, accountFromDb.getCurrency());
    }

    @Test
    public void checkUpdateCurrency() throws SQLException {
        BigDecimal balance = new BigDecimal(100500);
        String currency = "GBP";
        Account account = new Account();
        account.setId(1L);
        account.setCurrency(currency);

        accountRepository.updateAccount(account);
        // already tested by another tests
        Account accountFromDb = accountRepository.findAccountById(1L);

        assertEquals(balance, accountFromDb.getBalance());
        assertEquals(currency, accountFromDb.getCurrency());
    }

    @Test
    public void checkWillNotUpdateCreatedAt() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date updateDate = format.parse("2020-10-10 11:00:00");
        Date expectedDate = format.parse("2019-05-17 17:00:00");
        Account account = new Account();
        account.setId(1L);
        account.setCreatedAt(updateDate);

        accountRepository.updateAccount(account);
        // already tested by another tests
        Account accountFromDb = accountRepository.findAccountById(1L);

        assertEquals(expectedDate, accountFromDb.getCreatedAt());
    }

    @Test
    public void checkWillNotUpdateAccountUserId() throws SQLException {
        Long expectedUserId = 1L;
        Account account = new Account();
        account.setId(1L);
        account.setUserId(expectedUserId);

        accountRepository.updateAccount(account);
        // already tested by another tests
        Account accountFromDb = accountRepository.findAccountById(1L);

        assertEquals(expectedUserId, accountFromDb.getUserId());
    }

    @Test(expected = NoSuchEntityException.class)
    public void checkThatNoAccountAfterDelete() throws SQLException {
        accountRepository.deleteAccount(3L);
        accountRepository.findAccountById(3L);
    }

    @Test
    public void checkThatTwoAccountsLeftAfterDelete() throws SQLException {
        accountRepository.deleteAccount(3L);
        List<Account> accounts = accountRepository.findAllAccounts();
        assertEquals(2, accounts.size());
    }
}
