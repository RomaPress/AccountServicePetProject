package com.pres.account.service;

import com.pres.account.client.CurrencyExchangeClient;
import com.pres.account.model.dto.CurrencyExchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {
    private final CurrencyExchangeClient currencyExchangeClient;
    public static final Map<Integer, String> CURRENCY_MAPPING = new HashMap<>();

    static {
        Currency.getAvailableCurrencies()
                .forEach(x -> CURRENCY_MAPPING.put(x.getNumericCode(), x.getCurrencyCode()));
    }

    public List<CurrencyExchange> getCurrencyExchange() {
        return currencyExchangeClient.getCurrencyExchange().stream()
                .peek(x -> x.setCurrencyCodeA(CURRENCY_MAPPING.get(Integer.parseInt(x.getCurrencyCodeA()))))
                .peek(x -> x.setCurrencyCodeB(CURRENCY_MAPPING.get(Integer.parseInt(x.getCurrencyCodeB()))))
                .toList();
    }
}
