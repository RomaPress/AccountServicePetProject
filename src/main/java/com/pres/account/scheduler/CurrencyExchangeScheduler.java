package com.pres.account.scheduler;

import com.pres.account.model.dto.CurrencyExchange;
import com.pres.account.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CurrencyExchangeScheduler {
    private final CurrencyExchangeService currencyExchangeService;
    private List<CurrencyExchange> currencies;

    @Scheduled(fixedRateString = "${monobank.schedule.millis}")
    public void refreshCurrencyExchange() {
        currencies = currencyExchangeService.getCurrencyExchange();
    }

    public List<CurrencyExchange> getCurrencies() {
        if (currencies == null) {
            currencies = new ArrayList<>();
        }
        return currencies;
    }
}
