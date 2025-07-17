package com.andjelko.moneytransferservice.service;

import com.andjelko.moneytransferservice.datasource.DatabaseDataSource;
import com.andjelko.moneytransferservice.datasource.entities.TransactionEntity;
import com.andjelko.moneytransferservice.datasource.exceptions.DataSourceException;
import com.andjelko.moneytransferservice.datasource.exceptions.RetryableDataSourceException;
import com.andjelko.moneytransferservice.service.error.AccountNotFoundException;
import com.andjelko.moneytransferservice.service.error.InsufficientFundsException;
import com.andjelko.moneytransferservice.service.error.ServiceException;
import com.andjelko.moneytransferservice.service.mapper.TransactionServiceMapper;
import com.andjelko.moneytransferservice.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service for handling business logic for money transfers.
 * This service uses DatabaseDataSource to interact with the datasource layer and service mappers
 * to convert between service domain models and datasource models.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final DatabaseDataSource databaseDataSource;
    private final TransactionServiceMapper transactionServiceMapper;

    @Transactional
    @Retryable(
            value = {RetryableDataSourceException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public Transaction transferMoney(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {
        log.info("Initiating transfer of {} from account {} to account {}",
                amount, sourceAccountId, destinationAccountId);

        validateTransferParameters(sourceAccountId, destinationAccountId, amount);

        try {
            TransactionEntity transactionEntity = databaseDataSource.transferMoney(sourceAccountId, destinationAccountId, amount);

            Transaction transaction = transactionServiceMapper.toDomain(transactionEntity);

            log.info("Transfer completed successfully. Transaction ID: {}", transaction.getId());
            return transaction;
        } catch (DataSourceException e) {
            if (e.getMessage().contains("not found")) {
                throw new AccountNotFoundException(e.getMessage(), e);
            } else if (e.getMessage().contains("Insufficient funds")) {
                throw new InsufficientFundsException(e.getMessage(), e);
            } else if (e instanceof RetryableDataSourceException) {
                throw e;
            } else {
                throw new ServiceException("Data access error: " + e.getMessage(), e);
            }
        }
    }

    private void validateTransferParameters(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {
        if (sourceAccountId == null) {
            throw new IllegalArgumentException("Source account ID cannot be null");
        }
        if (destinationAccountId == null) {
            throw new IllegalArgumentException("Destination account ID cannot be null");
        }
        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
    }
}
