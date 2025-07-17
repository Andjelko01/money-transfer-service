package com.andjelko.moneytransferservice.datasource;

import com.andjelko.moneytransferservice.datasource.entities.AccountEntity;
import com.andjelko.moneytransferservice.datasource.entities.TransactionEntity;
import com.andjelko.moneytransferservice.datasource.error.DatasourceRetryPolicy;
import com.andjelko.moneytransferservice.datasource.exceptions.DataSourceException;
import com.andjelko.moneytransferservice.datasource.exceptions.NonRetryableDataSourceException;
import com.andjelko.moneytransferservice.datasource.exceptions.RetryableDataSourceException;
import com.andjelko.moneytransferservice.datasource.repository.AccountRepository;
import com.andjelko.moneytransferservice.datasource.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Database Data Source for handling data persistence operations.
 * This class is part of the service layer and works directly with entities.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseDataSource {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionAudit transactionAudit;

    public AccountEntity createAccount(AccountEntity accountEntity) {
        try {
            log.info("Creating account for: {}", accountEntity.getAccountHolderName());

            return accountRepository.save(accountEntity);
        } catch (Exception e) {
            throw mapError(e);
        }
    }

    public Optional<AccountEntity> findAccountById(Long id) {
        try {
            log.debug("Finding account by ID: {}", id);
            
            return accountRepository.findById(id);
        } catch (Exception e) {
            throw mapError(e);
        }
    }

    public List<AccountEntity> findAllAccounts() {
        try {
            log.debug("Finding all accounts");
            
            return accountRepository.findAll();
        } catch (Exception e) {
            throw mapError(e);
        }
    }

    @Transactional
    public TransactionEntity transferMoney(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {
        try {
            log.info("Transferring {} from account {} to account {}", amount, sourceAccountId, destinationAccountId);
            
            AccountEntity sourceAccount = accountRepository.findByIdWithLock(sourceAccountId)
                    .orElseThrow(() -> new NonRetryableDataSourceException("Source account not found"));

            AccountEntity destinationAccount = accountRepository.findById(destinationAccountId)
                    .orElseThrow(() -> new NonRetryableDataSourceException("Destination account not found"));

            if (sourceAccount.getBalance().compareTo(amount) < 0) {
                throw new NonRetryableDataSourceException("Insufficient funds");
            }
            
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
            
            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);
            
            return transactionAudit.saveSuccessfulTransaction(sourceAccountId, destinationAccountId, amount);

        } catch (Exception e) {
            log.error("An unexpected error occurred during the transfer: {}", e.getMessage(), e);
            try {
                transactionAudit.saveFailedTransaction(sourceAccountId, destinationAccountId, amount, e);
            } catch (Exception auditException) {
                log.error("CRITICAL: Failed to save audit record for a failed transaction.", auditException);
            }
            throw mapError(e);
        }
    }

    public List<TransactionEntity> findTransactionsByAccountId(Long accountId) {
        try {
            log.debug("Finding transactions for account ID: {}", accountId);
            
            return transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId);
        } catch (Exception e) {
            throw mapError(e);
        }
    }

    private DataSourceException mapError(Exception e) {
        log.error("Data access error: {}", e.getMessage(), e);

        if (e instanceof PessimisticLockingFailureException ||
            e instanceof QueryTimeoutException ||
            e instanceof TransientDataAccessResourceException ||
            e instanceof RecoverableDataAccessException) {
            return new RetryableDataSourceException(e.getMessage(), e);
        } else {
            return new NonRetryableDataSourceException(e.getMessage(), e);
        }
    }
}
