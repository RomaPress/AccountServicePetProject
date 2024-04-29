package com.pres.account.service;

import com.pres.account.model.dao.Account;
import com.pres.account.repositiry.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account sava(Account account) {
        return accountRepository.saveAndFlush(account);
    }
}
