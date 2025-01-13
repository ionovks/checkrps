package com.belivein1000rpsusingjava.rpstest;

import com.belivein1000rpsusingjava.rpstest.dto.AccountRepository;
import com.belivein1000rpsusingjava.rpstest.dto.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;




@RestController
@RequestMapping("/api")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    // GET http://localhost:9098/api/balance/1
    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long userId) {
        BigDecimal computedBalance = balanceService.getComputedBalance(userId);
        return ResponseEntity.ok(computedBalance);
    }
}