package com.bank.account.infrastructure.web.controller;

import com.bank.account.application.dto.response.AccountResponse;
import com.bank.account.application.dto.response.TransactionResponse;
import com.bank.account.application.mapper.AccountMapper;
import com.bank.account.application.mapper.TransactionMapper;
import com.bank.account.application.port.in.CreateAccountPort;
import com.bank.account.application.port.in.DepositPort;
import com.bank.account.application.port.in.GetAccountPort;
import com.bank.account.application.port.in.WithdrawPort;
import com.bank.account.application.usecase.*;
import com.bank.account.infrastructure.web.dto.CreateAccountRequest;
import com.bank.account.infrastructure.web.dto.DepositRequest;
import com.bank.account.infrastructure.web.dto.WithdrawRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final CreateAccountPort createAccountUseCase;
    private final GetAccountPort getAccountUseCase;
    private final DepositPort depositUseCase;
    private final WithdrawPort withdrawUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase,
                             GetAccountUseCase getAccountUseCase,
                             DepositUseCase depositUseCase,
                             WithdrawUseCase withdrawUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {

        var account = createAccountUseCase.execute(
                request.clientId(),
                request.type().name()
        );

        return ResponseEntity
                .status(201)
                .body(AccountMapper.toResponse(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id) {

        var account = getAccountUseCase.execute(id);

        return ResponseEntity.ok(AccountMapper.toResponse(account));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable Long id,
                                                       @Valid @RequestBody DepositRequest request) {
        log.info("Deposit request received: {}", request);

        var transaction = depositUseCase.execute(id, request.amount(), request.reference());

        return ResponseEntity.ok(TransactionMapper.toResponse(transaction));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable Long id,
                                         @Valid @RequestBody WithdrawRequest request) {

        var transaction = withdrawUseCase.execute(id, request.amount(), request.reference());

        return ResponseEntity.ok(TransactionMapper.toResponse(transaction));
    }
}