package com.belivein1000rpsusingjava.rpstest;

import com.belivein1000rpsusingjava.rpstest.dto.Account;
import com.belivein1000rpsusingjava.rpstest.dto.AccountRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAccounts(AccountRepository accountRepository) {
        return args -> {

            if (!accountRepository.existsById(1L)) {
                Account account1 = new Account();
                account1.setUserId(1L);
                account1.setCurrency("USD");
                account1.setCreatedAt(OffsetDateTime.now());
                account1.setUpdatedAt(OffsetDateTime.now());
                accountRepository.save(account1);
            }

            if (!accountRepository.existsById(2L)) {
                Account account2 = new Account();
                account2.setUserId(2L);
                account2.setCurrency("USD");
                account2.setCreatedAt(OffsetDateTime.now());
                account2.setUpdatedAt(OffsetDateTime.now());
                accountRepository.save(account2);
            }

            if (!accountRepository.existsById(3L)) {
                Account account3 = new Account();
                account3.setUserId(3L);
                account3.setCurrency("USD");
                account3.setCreatedAt(OffsetDateTime.now());
                account3.setUpdatedAt(OffsetDateTime.now());
                accountRepository.save(account3);
            }

            System.out.println("Аккаунты инициализированы с увеличенными балансами.");
        };
    }
}
