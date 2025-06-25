package com.kirana.register.controller;

import com.kirana.register.kafka.consumer.ReportConsumer;
import com.kirana.register.kafka.dto.ReportResult;
import com.kirana.register.model.Transaction;
import com.kirana.register.model.User;
import com.kirana.register.service.RateLimiterService;
import com.kirana.register.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.kirana.register.repository.UserRepository;
import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")

public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private UserRepository userRepository;

    public static class TransactionRequest {
        public BigDecimal amount;
        public String currency;
        public String type; 
    }

    @PostMapping
    public ResponseEntity<Transaction> recordTransaction(@RequestBody TransactionRequest request) {
        System.out.println("DEBUG: request.amount = " + request.amount);
        System.out.println("DEBUG: request.currency = " + request.currency);
        System.out.println("DEBUG: request.type = " + request.type);

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            User user = userRepository.findByEmail(email);
            if (user == null) {
                System.out.println("User not found.");
                return ResponseEntity.badRequest().build();
            }

            if (!rateLimiterService.isAllowed(user.getId())) {
                return ResponseEntity.status(429).body(null); 
            }
            Transaction transaction = transactionService.recordTransaction(
                    user,
                    request.amount,
                    request.currency,
                    request.type
            );

            System.out.println("Transaction created with ID: " + transaction.getId());

            return ResponseEntity.ok(transaction);

        } catch (Exception e) {
            System.out.println("CONTROLLER ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(user.getBalance());
    }

    @GetMapping("/report")
    public ResponseEntity<?> getReport(@RequestParam String range) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        ReportResult result = ReportConsumer.getCachedReport(user.getId(), range.toLowerCase());
        if (result == null) {
            return ResponseEntity.status(202).body("Report is still being generated. Please try again shortly.");
        }

        return ResponseEntity.ok(result);
    }
}
