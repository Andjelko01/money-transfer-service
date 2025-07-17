package com.andjelko.moneytransferservice.service.error;

public class AccountNotFoundException extends ServiceException {
    public AccountNotFoundException(String message) {
        super(message);
    }
    
    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
