package com.pres.account.client;


import com.pres.account.model.dto.CurrencyExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class CurrencyExchangeClient {
    private static final RestClient restClient = RestClient.create();

    @Value("${monobank.currency.exchange.api}")
    private String monobankCurrencyApi;

    public List<CurrencyExchange> getCurrencyExchange() {
        return restClient.get()
                .uri(monobankCurrencyApi)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

}
