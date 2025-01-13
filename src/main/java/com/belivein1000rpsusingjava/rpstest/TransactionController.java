package com.belivein1000rpsusingjava.rpstest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransferService transactionService;

    public TransactionController(TransferService transactionService) {
        this.transactionService = transactionService;
    }

    // Пример: POST http://localhost:8080/api/transfer?senderId=1&receiverId=2&amountInCents=100
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam Long amountInCents) {
        boolean success = transactionService.transferFunds(senderId, receiverId, BigDecimal.valueOf(amountInCents));
        if (success) {
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("FAIL");
        }
    }
}