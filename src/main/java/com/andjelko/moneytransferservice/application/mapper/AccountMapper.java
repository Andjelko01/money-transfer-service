package com.andjelko.moneytransferservice.application.mapper;

import com.andjelko.moneytransferservice.application.dto.AccountDto;
import com.andjelko.moneytransferservice.application.dto.CreateAccountRequest;
import com.andjelko.moneytransferservice.service.model.Account;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between application layer DTOs and service layer domain models.
 * This mapper is part of the application layer and handles the conversion
 * between DTOs (used by controllers) and domain models (used by services).
 */
@Component
public class AccountMapper {

    /**
     * Convert CreateAccountRequest DTO to Account domain model
     * @param request the request DTO
     * @return the domain model
     */
    public Account toDomain(CreateAccountRequest request) {
        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getInitialBalance());
        return account;
    }

    /**
     * Convert Account domain model to AccountDto
     * @param account the domain model
     * @return the DTO
     */
    public AccountDto toDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getAccountHolderName())
                .balance(account.getBalance())
                .build();
    }
}
