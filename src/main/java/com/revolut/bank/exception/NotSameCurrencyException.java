package com.revolut.bank.exception;

public class NotSameCurrencyException extends RuntimeException {
    public NotSameCurrencyException(String message) {
        super(message);
    }
}
