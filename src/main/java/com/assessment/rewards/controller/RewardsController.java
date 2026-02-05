package com.assessment.rewards.controller;

import com.assessment.rewards.model.RewardResponse;
import com.assessment.rewards.service.impl.RewardsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rewards")
@RequiredArgsConstructor
@Tag(name = "Rewards Controller", description = "Endpoints for rewards calculation")
public class RewardsController {

    private final RewardsServiceImpl service;

    @GetMapping
    @Operation(summary = "Get reward summary for all customers",
            responses = @ApiResponse(responseCode = "200", description = "List of rewards",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardResponse.class))))
    public List<RewardResponse> getAllRewards() {
        return service.getAllRewards();
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get reward summary for a specific customer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reward for customer",
                            content = @Content(schema = @Schema(implementation = RewardResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid customer ID",
                            content = @Content)
            })
    public RewardResponse getRewardsByCustomer(@PathVariable @NotBlank(message = "Customer ID must not be blank") String customerId) {
        return service.getRewardsByCustomerId(customerId);
    }
}

