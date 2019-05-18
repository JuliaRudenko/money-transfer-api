package com.revolut.bank.service;

import com.revolut.bank.model.Account;
import com.revolut.bank.repository.AccountRepository;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private AccountRepository accountRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
    }

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAllAccounts() throws SQLException {
        return accountRepository.findAllAccounts();
    }

    public Account findAccountById(Long id) throws SQLException {
        return accountRepository.findAccountById(id);
    }
}
