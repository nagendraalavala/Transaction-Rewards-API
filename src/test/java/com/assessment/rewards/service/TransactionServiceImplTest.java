package com.assessment.rewards.service;

import com.assessment.rewards.entity.Transaction;
import com.assessment.rewards.model.TransactionRequest;
import com.assessment.rewards.repository.TransactionRepository;
import com.assessment.rewards.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessTransaction_success() {
        TransactionRequest request = new TransactionRequest(
                "cust123",
                120,
                LocalDate.now()
        );

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(1L);
        savedTransaction.setAmount(request.amount());
        savedTransaction.setCustomerId(request.customerId());
        savedTransaction.setDate(request.date());
        savedTransaction.setRewards(90); // assuming calculatePointsForTransaction(120) = 90

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Transaction result = transactionService.processTransaction(request);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo("cust123");
        assertThat(result.getAmount()).isEqualTo(120);
        assertThat(result.getRewards()).isEqualTo(90);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testGetFilteredTransactions_withCustomerId() {
        Transaction t1 = new Transaction();
        t1.setCustomerId("cust1");
        t1.setAmount(100);

        Transaction t2 = new Transaction();
        t2.setCustomerId("cust1");
        t2.setAmount(200);

        List<Transaction> transactions = Arrays.asList(t1, t2);

        when(transactionRepository.findByCustomerId("cust1"))
                .thenReturn(Optional.of(transactions));

        List<Transaction> result = transactionService.getFilteredTransactions("cust1", 0, 10);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCustomerId()).isEqualTo("cust1");

        verify(transactionRepository, times(1)).findByCustomerId("cust1");
        verify(transactionRepository, never()).findAll();
    }

    @Test
    void testGetFilteredTransactions_withoutCustomerId() {
        Transaction t1 = new Transaction();
        t1.setCustomerId("cust1");
        t1.setAmount(100);

        Transaction t2 = new Transaction();
        t2.setCustomerId("cust2");
        t2.setAmount(200);

        List<Transaction> allTransactions = Arrays.asList(t1, t2);

        when(transactionRepository.findAll()).thenReturn(allTransactions);

        List<Transaction> result = transactionService.getFilteredTransactions(null, 0, 10);

        assertThat(result).hasSize(2);

        verify(transactionRepository, times(1)).findAll();
        verify(transactionRepository, never()).findByCustomerId(anyString());
    }

    @Test
    void testGetFilteredTransactions_pagination() {
        // 5 transactions for same customer
        Transaction t1 = new Transaction();
        t1.setCustomerId("cust1");
        Transaction t2 = new Transaction();
        t2.setCustomerId("cust1");
        Transaction t3 = new Transaction();
        t3.setCustomerId("cust1");
        Transaction t4 = new Transaction();
        t4.setCustomerId("cust1");
        Transaction t5 = new Transaction();
        t5.setCustomerId("cust1");

        List<Transaction> transactions = Arrays.asList(t1, t2, t3, t4, t5);

        when(transactionRepository.findByCustomerId("cust1")).thenReturn(Optional.of(transactions));

        // page=0, size=2 -> first 2
        List<Transaction> page1 = transactionService.getFilteredTransactions("cust1", 0, 2);
        assertThat(page1).hasSize(2);

        // page=1, size=2 -> next 2
        List<Transaction> page2 = transactionService.getFilteredTransactions("cust1", 1, 2);
        assertThat(page2).hasSize(2);

        // page=2, size=2 -> last 1
        List<Transaction> page3 = transactionService.getFilteredTransactions("cust1", 2, 2);
        assertThat(page3).hasSize(1);

        verify(transactionRepository, times(3)).findByCustomerId("cust1");
    }

    @Test
    void testProcessTransaction_repositoryThrowsException_shouldThrow() {
        TransactionRequest request = new TransactionRequest(
                "cust123",
                120,
                LocalDate.now()
        );

        when(transactionRepository.save(any(Transaction.class))).thenThrow(new RuntimeException("DB failure"));

        assertThatThrownBy(() -> transactionService.processTransaction(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB failure");

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testGetFilteredTransactions_withCustomerId_repositoryThrowsException_shouldThrow() {
        when(transactionRepository.findByCustomerId("cust1")).thenThrow(new RuntimeException("DB failure"));

        assertThatThrownBy(() -> transactionService.getFilteredTransactions("cust1", 0, 10))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB failure");

        verify(transactionRepository).findByCustomerId("cust1");
    }

    @Test
    void testGetFilteredTransactions_withoutCustomerId_repositoryThrowsException_shouldThrow() {
        when(transactionRepository.findAll()).thenThrow(new RuntimeException("DB failure"));

        assertThatThrownBy(() -> transactionService.getFilteredTransactions(null, 0, 10))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB failure");

        verify(transactionRepository).findAll();
    }

}
