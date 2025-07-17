package com.andjelko.moneytransferservice.service.error;

public class InsufficientFundsException extends ServiceException {
    public InsufficientFundsException(String message) {
        super(message);
    }
    
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
