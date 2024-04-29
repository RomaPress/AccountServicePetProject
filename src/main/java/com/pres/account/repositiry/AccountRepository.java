package com.pres.account.repositiry;

import com.pres.account.model.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountName(String accountName);

    boolean existsByAccountName(String accountName);
    boolean existsByAccountNameAndUserId(String accountName, Long userId);

    @Modifying()
    @Query(nativeQuery = true,
            value ="UPDATE accounts SET balance = balance + :amount WHERE account_name = :accountName")
    void increaseBalance(String accountName, BigDecimal amount);
}
