package com.assessment.rewards.controller;

import com.assessment.rewards.model.RewardResponse;
import com.assessment.rewards.service.impl.RewardsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Month;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RewardsController.class)
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsServiceImpl rewardsService;

    private RewardResponse sampleReward;

    @BeforeEach
    void setup() {
        sampleReward = new RewardResponse(
                "cust1",
                Map.of(Month.MARCH, 100),
                100
        );
    }

    @Test
    void testGetAllRewards() throws Exception {
        List<RewardResponse> rewards = List.of(sampleReward);
        when(rewardsService.getAllRewards()).thenReturn(rewards);

        mockMvc.perform(get("/v1/rewards")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("cust1"))
                .andExpect(jsonPath("$[0].totalPoints").value(100));

        verify(rewardsService, times(1)).getAllRewards();
    }

    @Test
    void testGetRewardsByCustomer_validCustomer() throws Exception {
        when(rewardsService.getRewardsByCustomerId("cust1")).thenReturn(sampleReward);

        mockMvc.perform(get("/v1/rewards/cust1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("cust1"))
                .andExpect(jsonPath("$.totalPoints").value(100));

        verify(rewardsService, times(1)).getRewardsByCustomerId("cust1");
    }
}
