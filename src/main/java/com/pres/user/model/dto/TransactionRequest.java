package com.pres.user.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Long fromUserId;
    private String fromAccountName;
    private String toAccountName;
    private BigDecimal amount;
}
