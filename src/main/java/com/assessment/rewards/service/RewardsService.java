package com.assessment.rewards.service;

import com.assessment.rewards.model.RewardResponse;

import java.util.List;

public interface RewardsService {
     List<RewardResponse> getAllRewards();
     RewardResponse getRewardsByCustomerId(String customerId);
}
