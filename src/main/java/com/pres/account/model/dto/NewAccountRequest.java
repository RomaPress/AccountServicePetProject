package com.pres.account.model.dto;

import lombok.Data;
import com.pres.account.model.dao.Account;

import java.math.BigDecimal;

@Data
public class NewAccountRequest {
    private Long userId;
    private String accountName;
    private BigDecimal balance;
    private String currency;

    public Account toAccount() {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountName(accountName);
        account.setBalance(balance);
        account.setCurrency(currency);
        return account;
    }
}
