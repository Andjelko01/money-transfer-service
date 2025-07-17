package com.andjelko.moneytransferservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Account information")
public class AccountDto {
    
    @Schema(description = "Account ID", example = "1")
    private Long id;
    
    @Schema(description = "Account number", example = "ACC-123456789")
    private String accountNumber;
    
    @Schema(description = "Account holder name", example = "John Doe")
    private String accountHolderName;
    
    @Schema(description = "Account balance", example = "1000.50")
    private BigDecimal balance;
}