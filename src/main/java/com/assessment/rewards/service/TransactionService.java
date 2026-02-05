package com.assessment.rewards.service;

import com.assessment.rewards.entity.Transaction;
import com.assessment.rewards.model.TransactionRequest;

import java.util.List;

public interface TransactionService {

    Transaction processTransaction(TransactionRequest request);
    List<Transaction> getFilteredTransactions(String customerId, int page, int size);
}
