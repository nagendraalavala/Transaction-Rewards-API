package com.assessment.rewards.model;

import java.time.Month;
import java.util.Map;

public record RewardResponse(
        String customerId,
        Map<Month, Integer> monthlyPoints,
        int totalPoints
) {
}