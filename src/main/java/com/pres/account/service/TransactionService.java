package com.pres.account.service;

import com.pres.account.model.dao.Account;
import com.pres.account.model.dao.Transaction;
import com.pres.account.model.dto.AccountResponse;
import com.pres.account.model.dto.CurrencyExchange;
import com.pres.account.model.dto.TransactionRequest;
import com.pres.account.repositiry.AccountRepository;
import com.pres.account.repositiry.TransactionRepository;
import com.pres.account.scheduler.CurrencyExchangeScheduler;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final EntityManager entityManager;
    private final CurrencyExchangeScheduler exchangeScheduler;


    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = AccountException.class, propagation = Propagation.REQUIRED)
    public AccountResponse executeTransaction(TransactionRequest request) throws AccountException {
        Long userId = request.getFromUserId();
        String sourceAccountName = request.getFromAccountName();
        String targetAccountName = request.getToAccountName();
        BigDecimal amount = request.getAmount();

        if (!accountRepository.existsByAccountNameAndUserId(sourceAccountName, userId)) {
            throw new AccountNotFoundException("Source account does not exist");
        }
        if (!accountRepository.existsByAccountName(targetAccountName)) {
            throw new AccountNotFoundException("Target account does not exist");
        }
        Account sourceAccount = accountRepository.findByAccountName(sourceAccountName);
        Account targetAccount = accountRepository.findByAccountName(targetAccountName);
        BigDecimal exchangeAmount = calculateTransferAmount(sourceAccount, targetAccount, amount);

        transactionRepository.saveAndFlush(Transaction.builder()
                .fromAccount(sourceAccount)
                .toAccount(targetAccount)
                .amount(exchangeAmount).build());
        accountRepository.increaseBalance(targetAccountName, exchangeAmount);
        accountRepository.increaseBalance(sourceAccountName, amount.negate());

        entityManager.refresh(sourceAccount);
        return AccountResponse.builder()
                .accountName(sourceAccountName)
                .balance(sourceAccount.getBalance())
                .currency(sourceAccount.getCurrency()).build();
    }

    private BigDecimal calculateTransferAmount(Account sourceAccount, Account targetAccount, BigDecimal amount) throws AccountException {
        BigDecimal sourceBalance = sourceAccount.getBalance();
        if (sourceBalance.compareTo(amount) < 0) {
            throw new AccountException("Money exceeds limit");
        }
        String sourceCurrency = sourceAccount.getCurrency();
        String targetCurrency = targetAccount.getCurrency();
        if (StringUtils.endsWith(sourceCurrency, targetCurrency)) {
            return amount;
        }

        Optional<CurrencyExchange> exchange = exchangeScheduler.getCurrencies().stream()
                .filter(x -> isExchangeFit(sourceCurrency, targetCurrency, x))
                .findFirst();
        if (exchange.isEmpty()) {
            throw new AccountException("Currency exchange not supported");
        }
        boolean isMultiplyRate = isMultiplyRate(exchange.get(), sourceCurrency, targetCurrency);
        Function<BigDecimal, BigDecimal> action = collectExchangeFunction(amount, isMultiplyRate);
        return retrieveExchangeRate(exchange.get(), amount, action);
    }

    private Function<BigDecimal, BigDecimal> collectExchangeFunction(BigDecimal amount, boolean isMultiplyRate){
        return  rate -> {
            if (isMultiplyRate) {
                return amount.multiply(rate);
            }
            return amount.divide(rate, 2, RoundingMode.HALF_UP);
        };
    }

    private BigDecimal retrieveExchangeRate(CurrencyExchange exchange, BigDecimal amount, Function<BigDecimal, BigDecimal> action) {
        if (Objects.nonNull(exchange.getRateCross())) {
            return action.apply(exchange.getRateCross());
        }
        return action.apply(exchange.getRateSell());
    }

    private boolean isExchangeFit(String sourceCurrency, String targetCurrency, CurrencyExchange exchange) {
        return isDivideRate(exchange, sourceCurrency, targetCurrency) ||
                isMultiplyRate(exchange, sourceCurrency, targetCurrency);
    }

    private boolean isDivideRate(CurrencyExchange exchange, String sourceCurrency, String targetCurrency) {
        return exchange.getCurrencyCodeA().equals(targetCurrency) &&
                exchange.getCurrencyCodeB().equals(sourceCurrency);
    }

    private boolean isMultiplyRate(CurrencyExchange exchange, String sourceCurrency, String targetCurrency) {
        return exchange.getCurrencyCodeA().equals(sourceCurrency) &&
                exchange.getCurrencyCodeB().equals(targetCurrency);
    }
}
