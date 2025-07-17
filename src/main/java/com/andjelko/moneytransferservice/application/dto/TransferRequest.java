package com.andjelko.moneytransferservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to transfer money between accounts")
public class TransferRequest {

    @NotNull(message = "Source account ID cannot be null")
    @Schema(description = "Source account ID", example = "1")
    private Long sourceAccountId;
    
    @NotNull(message = "Destination account ID cannot be null")
    @Schema(description = "Destination account ID", example = "2")
    private Long destinationAccountId;
    
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    @Schema(description = "Amount to transfer", example = "100.50")
    private BigDecimal amount;

}
