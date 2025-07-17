package com.andjelko.moneytransferservice.application.controller;

import com.andjelko.moneytransferservice.AbstractIntegrationTest;
import com.andjelko.moneytransferservice.application.dto.AccountDto;
import com.andjelko.moneytransferservice.application.dto.CreateAccountRequest;
import com.andjelko.moneytransferservice.application.dto.TransferRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanup() {
        jdbcTemplate.execute("DELETE FROM transactions");
        jdbcTemplate.execute("DELETE FROM accounts");
    }

    @Test
    void shouldTransferMoneySuccessfully() throws Exception {
        AccountDto sourceAccount = createAccount("ACC-123456782","Source Account", "1500.00");
        AccountDto destinationAccount = createAccount("ACC-123456781","Destination Account", "500.00");

        TransferRequest transactionRequest = new TransferRequest(
                sourceAccount.getId(),
                destinationAccount.getId(),
                new BigDecimal("300.00")
        );

        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/accounts/" + sourceAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1200.00));

        mockMvc.perform(get("/api/v1/accounts/" + destinationAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800.00));
    }

    @Test
    void shouldFailTransferWhenInsufficientFunds() throws Exception {
        AccountDto sourceAccount = createAccount("ACC-123456781","Source Account", "100.00");
        AccountDto destinationAccount = createAccount("ACC-123456789","Destination Account", "500.00");

        TransferRequest transactionRequest = new TransferRequest(
                sourceAccount.getId(),
                destinationAccount.getId(),
                new BigDecimal("200.00")
        );

        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/accounts/" + sourceAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100.00));

        mockMvc.perform(get("/api/v1/accounts/" + destinationAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.00));
    }

    private AccountDto createAccount(String accountNumber,String holderName, String balance) throws Exception {
        CreateAccountRequest request = new CreateAccountRequest(accountNumber ,holderName, new BigDecimal(balance));
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), AccountDto.class);
    }
}
