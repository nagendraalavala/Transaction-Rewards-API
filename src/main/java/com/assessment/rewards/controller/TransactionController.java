package com.assessment.rewards.controller;

import com.assessment.rewards.entity.Transaction;
import com.assessment.rewards.model.TransactionRequest;
import com.assessment.rewards.service.impl.TransactionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/transactions/")
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "Endpoints for transactions")
@Slf4j
public class TransactionController {

    private final TransactionServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all customer transactions with optional pagination and filtering",
            responses = @ApiResponse(responseCode = "200", description = "List of transactions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class))))
    public List<Transaction> getAllTransactions(
            @Parameter(description = "Customer ID to filter transactions")
            @RequestParam(required = false) String customerId,
            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be 0 or greater") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size
    ) {
        return service.getFilteredTransactions(customerId, page, size);
    }


    @PostMapping("/add")
    @Operation(
            summary = "Add a new transaction",
            description = "Adds a new transaction and calculates reward points based on the transaction amount.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transaction request with customerId and amount",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction successfully processed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionRequest.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Transaction> addTransaction(
            @RequestBody @Valid
            @Parameter(description = "Transaction request body", required = true)
                    TransactionRequest request) {
        log.info("Received transaction request for customerId={}", request.customerId());
        Transaction transaction = service.processTransaction(request);
        return ResponseEntity.ok(transaction);
    }
}
