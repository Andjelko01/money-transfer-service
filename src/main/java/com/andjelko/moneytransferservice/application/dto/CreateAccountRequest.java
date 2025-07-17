package com.andjelko.moneytransferservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Schema(description = "Request to create a new account")
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotBlank(message = "Account number cannot be blank")
    @Size(min = 5, max = 20, message = "Account number must be between 5 and 20 characters")
    @Schema(description = "Account number", example = "ACC-123456789")
    private String accountNumber;

    @NotBlank(message = "Account holder name cannot be blank")
    @Size(min = 2, max = 100, message = "Account holder name must be between 2 and 100 characters")
    @Schema(description = "Account holder name", example = "John Doe")
    private String accountHolderName;

    @NotNull(message = "Initial balance cannot be null")
    @DecimalMin(value = "0.0", message = "Initial balance must be non-negative")
    @Schema(description = "Initial account balance", example = "1000.50")
    private BigDecimal initialBalance;
}
