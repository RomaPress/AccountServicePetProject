package com.pres.user.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {
    private String accountName;
    private String currency;
    private BigDecimal balance;
}
