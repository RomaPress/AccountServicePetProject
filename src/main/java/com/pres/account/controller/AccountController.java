package com.pres.account.controller;

import com.pres.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import com.pres.account.model.dao.Account;
import com.pres.account.model.dto.NewAccountRequest;
import com.pres.account.model.dto.AccountResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping()
    public AccountResponse createAccount(@RequestBody NewAccountRequest request) {
        Account account = accountService.sava(request.toAccount());
        return AccountResponse.fromAccount(account);
    }
}
