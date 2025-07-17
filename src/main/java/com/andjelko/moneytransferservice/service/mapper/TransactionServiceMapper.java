package com.andjelko.moneytransferservice.service.mapper;

import com.andjelko.moneytransferservice.datasource.entities.TransactionEntity;
import com.andjelko.moneytransferservice.service.model.Transaction;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between service domain models and datasource entities.
 * This mapper is part of the service layer and handles the conversion
 * between service layer domain models and datasource layer entities.
 */
@Component
public class TransactionServiceMapper {

    /**
     * Convert TransactionEntity to Transaction domain model
     * @param transactionEntity the datasource entity to convert
     * @return the domain model
     */
    public Transaction toDomain(TransactionEntity transactionEntity) {
        if (transactionEntity == null) {
            return null;
        }
        
        Transaction transaction = new Transaction();
        transaction.setId(transactionEntity.getId());
        transaction.setSourceAccountId(transactionEntity.getSourceAccountId());
        transaction.setDestinationAccountId(transactionEntity.getDestinationAccountId());
        transaction.setAmount(transactionEntity.getAmount());
        transaction.setTransactionTime(transactionEntity.getTransactionTime());
        transaction.setStatus(transactionEntity.getStatus());
        transaction.setMessage(transactionEntity.getMessage());
        return transaction;
    }

    /**
     * Convert Transaction domain model to TransactionEntity
     * @param transaction the domain model to convert
     * @return the datasource entity
     */
    public TransactionEntity toEntity(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        
        return TransactionEntity.builder()
                .id(transaction.getId())
                .sourceAccountId(transaction.getSourceAccountId())
                .destinationAccountId(transaction.getDestinationAccountId())
                .amount(transaction.getAmount())
                .transactionTime(transaction.getTransactionTime())
                .status(transaction.getStatus())
                .message(transaction.getMessage())
                .build();
    }
}
