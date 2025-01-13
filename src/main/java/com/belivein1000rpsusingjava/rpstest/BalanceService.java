package com.belivein1000rpsusingjava.rpstest;

import com.belivein1000rpsusingjava.rpstest.dto.Account;
import com.belivein1000rpsusingjava.rpstest.dto.AccountRepository;
import com.belivein1000rpsusingjava.rpstest.dto.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BalanceService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BalanceService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public BigDecimal getComputedBalance(Long userId) {
        Account account = accountRepository.findByUserId(userId);
        if (account == null) {
            throw new RuntimeException("Account not found for userId: " + userId);
        }


        return transactionRepository.calculateOperationsBalanceByUserId(userId);
    }
}