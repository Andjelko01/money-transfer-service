package com.andjelko.moneytransferservice.application.controller;

import com.andjelko.moneytransferservice.application.dto.AccountDto;
import com.andjelko.moneytransferservice.application.dto.CreateAccountRequest;
import com.andjelko.moneytransferservice.application.mapper.AccountMapper;
import com.andjelko.moneytransferservice.service.AccountService;
import com.andjelko.moneytransferservice.service.model.Account;
import com.andjelko.moneytransferservice.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Account management operations")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping
    @Operation(summary = "Create a new account", description = "Create a new account with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account accountToCreate = accountMapper.toDomain(request);
        Account createdAccount = accountService.createAccount(accountToCreate);
        AccountDto createdAccountDto = accountMapper.toDto(createdAccount);
        return new ResponseEntity<>(createdAccountDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve account details by account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        Optional<Account> accountOptional = accountService.findAccountById(id);
        
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            AccountDto accountDto = accountMapper.toDto(account);
            return ResponseEntity.ok(accountDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
