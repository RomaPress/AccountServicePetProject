package com.pres.account.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CurrencyExchangeResponse {
    private List<CurrencyExchange> currencies;
}
