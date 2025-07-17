package com.andjelko.moneytransferservice.application.controller;

import com.andjelko.moneytransferservice.AbstractIntegrationTest;
import com.andjelko.moneytransferservice.application.dto.AccountDto;
import com.andjelko.moneytransferservice.application.dto.CreateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
public class AccountControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAccountAndThenRetrieveIt() throws Exception {
        CreateAccountRequest createRequest = new CreateAccountRequest("ACB-123456789","Nikola Tesla", new BigDecimal("1000.00"));

        MvcResult createResult = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountHolderName").value("Nikola Tesla"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        AccountDto createdAccount = objectMapper.readValue(responseBody, AccountDto.class);
        assertThat(createdAccount.getId()).isNotNull();

        mockMvc.perform(get("/api/v1/accounts/" + createdAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAccount.getId()))
                .andExpect(jsonPath("$.accountHolderName").value("Nikola Tesla"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }
}
