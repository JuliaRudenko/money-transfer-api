package com.revolut.bank.exception;

public class NotSufficientBalanceException extends RuntimeException {
    public NotSufficientBalanceException(String message) {
        super(message);
    }
}
