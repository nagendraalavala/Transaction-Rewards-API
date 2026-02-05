package com.assessment.rewards.service;

import com.assessment.rewards.entity.Transaction;
import com.assessment.rewards.exception.CustomerNotFoundException;
import com.assessment.rewards.model.RewardResponse;
import com.assessment.rewards.repository.TransactionRepository;
import com.assessment.rewards.service.impl.RewardsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardsServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRewardsByCustomerId_Success() {
        var customerId = "cust123";
        var tx1 = new Transaction(1L, customerId, LocalDate.of(2024, 3, 10), 120.0, 90);
        var tx2 = new Transaction(2L, customerId, LocalDate.of(2024, 3, 15), 75.0, 25);

        when(transactionRepository.findByCustomerId(customerId)).thenReturn(Optional.of(List.of(tx1, tx2)));

        var response = rewardsService.getRewardsByCustomerId(customerId);

        assertNotNull(response);
        assertEquals(customerId, response.customerId());
        assertEquals(115, response.totalPoints());
        assertEquals(1, response.monthlyPoints().size());
        assertTrue(response.monthlyPoints().containsKey(Month.MARCH));
    }

    @Test
    void testGetRewardsByCustomerId_NotFound() {
        var customerId = "unknown";
        when(transactionRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> rewardsService.getRewardsByCustomerId(customerId));
    }


    @Test
    void testGetAllRewards() {
        var t1 = transaction("cust1", 120);
        var t2 = transaction("cust1", 80);
        var t3 = transaction("cust2", 200);

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(t1, t2, t3));

        // Act
        var rewards = rewardsService.getAllRewards();

        // Assert
        assertThat(rewards)
                .isNotNull()
                .hasSize(2)
                .extracting(RewardResponse :: customerId)
                .containsExactlyInAnyOrder("cust1", "cust2");

        verify(transactionRepository, times(1)).findAll();
    }

    private static Transaction transaction(String customerId, double amount) {
        var tx = new Transaction();
        tx.setCustomerId(customerId);
        tx.setAmount(amount);
        return tx;
    }

}