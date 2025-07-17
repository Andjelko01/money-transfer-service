package com.andjelko.moneytransferservice.datasource;

import com.andjelko.moneytransferservice.datasource.entities.TransactionEntity;
import com.andjelko.moneytransferservice.datasource.repository.TransactionRepository;
import com.andjelko.moneytransferservice.service.model.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * TransactionAudit for auditing transaction execution status.
 * Saves successful and failed transactions to the database for audit purposes.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionAudit {

    private final TransactionRepository transactionRepository;

    /**
     * Saves a successful transaction to the database for audit purposes.
     * 
     * @param sourceAccountId the source account ID
     * @param destinationAccountId the destination account ID
     * @param amount the transfer amount
     * @return the saved transaction entity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionEntity saveSuccessfulTransaction(Long sourceAccountId, Long destinationAccountId, 
                                                      BigDecimal amount) {
        TransactionEntity transaction = TransactionEntity.builder()
                .sourceAccountId(sourceAccountId)
                .destinationAccountId(destinationAccountId)
                .amount(amount)
                .transactionTime(OffsetDateTime.now())
                .status(TransactionStatus.SUCCESS)
                .message("Transaction completed successfully")
                .build();

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        
        log.info("AUDIT: Transaction SUCCESSFUL saved - ID: {}, Source: {}, Destination: {}, Amount: {}", 
                savedTransaction.getId(), sourceAccountId, destinationAccountId, amount);
        
        return savedTransaction;
    }

    /**
     * Saves a failed transaction with exception details to the database for audit purposes.
     * 
     * @param sourceAccountId the source account ID
     * @param destinationAccountId the destination account ID
     * @param amount the transfer amount
     * @param failureReason the reason for failure
     * @param exception the exception that caused the failure
     * @return the saved transaction entity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionEntity saveFailedTransaction(Long sourceAccountId, Long destinationAccountId, 
                                                  BigDecimal amount, Exception exception) {
        String detailedMessage = exception.getMessage();
        
        TransactionEntity transaction = TransactionEntity.builder()
                .sourceAccountId(sourceAccountId)
                .destinationAccountId(destinationAccountId)
                .amount(amount)
                .transactionTime(OffsetDateTime.now())
                .status(TransactionStatus.FAILED)
                .message(detailedMessage)
                .build();

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        
        log.error("AUDIT: Transaction FAILED saved - ID: {}, Source: {}, Destination: {}, Amount: {}, Exception: {}",
                savedTransaction.getId(), sourceAccountId, destinationAccountId, amount, exception.getMessage(), exception);
        
        return savedTransaction;
    }
}
