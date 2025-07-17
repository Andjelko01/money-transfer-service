package com.andjelko.moneytransferservice.service;

import com.andjelko.moneytransferservice.datasource.DatabaseDataSource;
import com.andjelko.moneytransferservice.datasource.entities.AccountEntity;
import com.andjelko.moneytransferservice.datasource.exceptions.DataSourceException;
import com.andjelko.moneytransferservice.datasource.exceptions.RetryableDataSourceException;
import com.andjelko.moneytransferservice.service.error.ServiceException;
import com.andjelko.moneytransferservice.service.mapper.AccountServiceMapper;
import com.andjelko.moneytransferservice.service.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Service for handling business logic for account management.
 * This service uses DatabaseDataSource to interact with the datasource layer and service mappers
 * to convert between service domain models and datasource models.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountServiceMapper accountServiceMapper;
    private final DatabaseDataSource databaseDataSource;


    @Retryable(
            value = {RetryableDataSourceException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public Account createAccount(Account account) {
        try {
            AccountEntity accountEntity = accountServiceMapper.toEntity(account);
            AccountEntity createdAccountEntity = databaseDataSource.createAccount(accountEntity);
            Account createdAccount = accountServiceMapper.toDomain(createdAccountEntity);

            log.info("Account created successfully with ID: {}", createdAccount.getId());
            return createdAccount;
        } catch (DataSourceException e) {
            log.error("Error creating account: {}", e.getMessage());
            if (e instanceof RetryableDataSourceException) {
                throw e;
            } else {
                throw new ServiceException("Error creating account: " + e.getLocalizedMessage());
            }
        }
    }

    public Optional<Account> findAccountById(Long id) {
        log.debug("Finding account by ID: {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        try {
            return databaseDataSource.findAccountById(id)
                    .map(accountServiceMapper::toDomain);
        } catch (DataSourceException e) {
            log.error("Error finding account by ID {}: {}", id, e.getMessage());
            throw new ServiceException("Error finding account: " + e.getMessage(), e);
        }
    }
}
