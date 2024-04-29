package com.pres.account.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyExchange {
    private String currencyCodeA;
    private String currencyCodeB;
    private Long date;
    private BigDecimal rateBuy;
    private BigDecimal rateSell;
    private BigDecimal rateCross;
}
