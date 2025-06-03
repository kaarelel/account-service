package com.kaarelel.accountservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaarelel.accountservice.entity.Account;
import com.kaarelel.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        testAccount = new Account();
        testAccount.setUsername("TestUser");
        testAccount.setPhone("58987565");
        accountRepository.save(testAccount);
    }

    @Test
    void testGetAccountById() throws Exception {
        mockMvc.perform(get("/accounts/" + testAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("TestUser"));
    }

    @Test
    void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setUsername("AnotherUser");
        account.setPhone("+37255551111");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("AnotherUser"));
    }

    @Test
    void testUpdateAccount() throws Exception {
        testAccount.setUsername("UpdatedName");

        mockMvc.perform(put("/accounts/" + testAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("UpdatedName"));
    }

    @Test
    void testDeleteAccount() throws Exception {
        mockMvc.perform(delete("/accounts/" + testAccount.getId()))
                .andExpect(status().isNoContent());
    }
}
