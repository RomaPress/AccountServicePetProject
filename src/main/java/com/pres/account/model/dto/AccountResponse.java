package com.pres.account.model.dto;

import com.pres.account.model.dao.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String accountName;
    private String currency;
    private BigDecimal balance;

    public static AccountResponse fromAccount(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountName(account.getAccountName());
        response.setCurrency(account.getCurrency());
        response.setBalance(account.getBalance());
        return response;
    }
}
