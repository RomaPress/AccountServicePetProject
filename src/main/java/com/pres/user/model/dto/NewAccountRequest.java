package com.pres.user.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class NewAccountRequest {
    private Long userId;
    private String accountName;
    private String currency;
    private BigDecimal balance;
}
