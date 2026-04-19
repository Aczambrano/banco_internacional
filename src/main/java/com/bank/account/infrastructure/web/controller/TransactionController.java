package com.bank.account.infrastructure.web.controller;

import com.bank.account.application.dto.response.TransactionResponse;
import com.bank.account.application.dto.response.TransferResponse;
import com.bank.account.application.mapper.TransactionMapper;
import com.bank.account.application.mapper.TransferMapper;
import com.bank.account.application.port.in.GetTransactionHistoryPort;
import com.bank.account.application.port.in.TransferPort;
import com.bank.account.application.usecase.GetTransactionHistoryUseCase;
import com.bank.account.application.usecase.TransferUseCase;
import com.bank.account.infrastructure.web.dto.TransferRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransferPort transferUseCase;
    private final GetTransactionHistoryPort historyUseCase;

    public TransactionController(TransferUseCase transferUseCase,
                                 GetTransactionHistoryUseCase historyUseCase) {
        this.transferUseCase = transferUseCase;
        this.historyUseCase = historyUseCase;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        request.validateDifferentAccounts();

        var transfer = transferUseCase.execute(
                request.sourceAccountId(),
                request.targetAccountId(),
                request.amount(),
                request.reference()
        );

        return ResponseEntity.ok(TransferMapper.fromTransfer(transfer));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getHistory(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var transactions = historyUseCase.execute(accountId, page, size);

        var response = transactions.stream()
                .map(TransactionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}