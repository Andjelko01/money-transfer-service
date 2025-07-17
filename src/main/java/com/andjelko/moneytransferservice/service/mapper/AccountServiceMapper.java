package com.andjelko.moneytransferservice.service.mapper;

import com.andjelko.moneytransferservice.datasource.entities.AccountEntity;
import com.andjelko.moneytransferservice.service.model.Account;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between service domain models and datasource entities.
 * This mapper is part of the service layer and handles the conversion
 * between service layer domain models and datasource layer entities.
 */
@Component
public class AccountServiceMapper {

    /**
     * Convert AccountEntity to Account domain model
     * @param accountEntity the datasource entity to convert
     * @return the domain model
     */
    public Account toDomain(AccountEntity accountEntity) {
        if (accountEntity == null) {
            return null;
        }
        
        Account account = new Account();
        account.setId(accountEntity.getId());
        account.setAccountNumber(accountEntity.getAccountNumber());
        account.setAccountHolderName(accountEntity.getAccountHolderName());
        account.setBalance(accountEntity.getBalance());
        return account;
    }

    /**
     * Convert Account domain model to AccountEntity
     * @param account the domain model to convert
     * @return the datasource entity
     */
    public AccountEntity toEntity(Account account) {
        if (account == null) {
            return null;
        }
        
        return AccountEntity.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getAccountHolderName())
                .balance(account.getBalance())
                .build();
    }
}
