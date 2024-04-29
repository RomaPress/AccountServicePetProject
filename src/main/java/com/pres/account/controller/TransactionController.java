package com.pres.account.controller;

import com.pres.account.model.dto.AccountResponse;
import com.pres.account.model.dto.TransactionRequest;
import com.pres.account.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping()
    public AccountResponse executeTransaction(@RequestBody @NonNull TransactionRequest request) throws AccountException {
        return transactionService.executeTransaction(request);
    }
}
