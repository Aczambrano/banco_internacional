package com.bank.account.infrastructure.web.controller;

import com.bank.account.application.dto.response.AccountResponse;
import com.bank.account.application.mapper.AccountMapper;
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

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;

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
    public ResponseEntity<Void> deposit(@PathVariable Long id,
                                        @Valid @RequestBody DepositRequest request) {
        log.info("Deposit request received: {}", request);

        depositUseCase.execute(id, request.amount());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long id,
                                         @Valid @RequestBody WithdrawRequest request) {

        withdrawUseCase.execute(id, request.amount());

        return ResponseEntity.ok().build();
    }
}