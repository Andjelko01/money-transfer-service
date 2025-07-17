package com.andjelko.moneytransferservice.application.controller;

import com.andjelko.moneytransferservice.application.dto.TransferRequest;
import com.andjelko.moneytransferservice.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Tag(name = "Transfer", description = "Money transfer operations")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @Operation(summary = "Transfer money between accounts", description = "Transfer a specified amount from source account to destination account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "422", description = "Insufficient funds"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> transferMoney(@Valid @RequestBody TransferRequest transferRequest) {
        transferService.transferMoney(
                transferRequest.getSourceAccountId(),
                transferRequest.getDestinationAccountId(),
                transferRequest.getAmount()
        );
        return ResponseEntity.ok().build();
    }
}
