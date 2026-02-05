package com.assessment.rewards.controller;

import com.assessment.rewards.entity.Transaction;
import com.assessment.rewards.model.TransactionRequest;
import com.assessment.rewards.service.impl.TransactionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction sampleTransaction;
    private TransactionRequest sampleRequest;

    @BeforeEach
    void setup() {
        sampleTransaction = new Transaction();
        sampleTransaction.setCustomerId("cust123");
        sampleTransaction.setAmount(150.0);
        sampleTransaction.setRewards(75);
        sampleTransaction.setDate(LocalDate.of(2025, 6, 22));

        sampleRequest = new TransactionRequest(
                "cust123",
                120,
                LocalDate.of(2025, 6, 22)
        );
    }

    @Test
    void testGetAllTransactions_withFilters() throws Exception {
        when(service.getFilteredTransactions(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/v1/transactions/")
                        .param("customerId", "cust123")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("cust123"))
                .andExpect(jsonPath("$[0].amount").value(150.0))
                .andExpect(jsonPath("$[0].rewards").value(75));
    }

    @Test
    void testGetAllTransactions_withoutFilters() throws Exception {
        when(service.getFilteredTransactions(null, 0, 10))
                .thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/v1/transactions/")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("cust123"));
    }

    @Test
    void testAddTransaction_validRequest() throws Exception {
        when(service.processTransaction(any(TransactionRequest.class))).thenReturn(sampleTransaction);

        mockMvc.perform(post("/v1/transactions/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("cust123"))
                .andExpect(jsonPath("$.amount").value(150.0))
                .andExpect(jsonPath("$.rewards").value(75));
    }

    @Test
    void testAddTransaction_invalidRequest_shouldReturn400() throws Exception {
        // Send empty JSON to simulate validation failure (missing required fields)
        mockMvc.perform(post("/v1/transactions/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

}

