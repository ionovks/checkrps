package com.belivein1000rpsusingjava.rpstest;

import com.belivein1000rpsusingjava.rpstest.dto.*;

import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

import java.math.BigDecimal;


@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public boolean transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElse(null);
        Account toAccount   = accountRepository.findById(toAccountId).orElse(null);

        if (fromAccount == null || toAccount == null) {
            return false;
        }

        TransactionEntity debitTransaction = new TransactionEntity(
                fromAccount,
                amount,
                TransactionType.debit,
                null,
                TransactionStatus.completed
        );
        transactionRepository.save(debitTransaction);

        TransactionEntity creditTransaction = new TransactionEntity(
                toAccount,
                amount,
                TransactionType.credit,
                debitTransaction.getTransactionId(),
                TransactionStatus.completed
        );
        transactionRepository.save(creditTransaction);

        return true;
    }
}