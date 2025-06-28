package com.kirana.register.service;

import com.kirana.register.model.Transaction;
import com.kirana.register.model.User;
import com.kirana.register.repository.TransactionRepository;
import com.kirana.register.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyConversionService currencyConversionService;

    public Transaction recordTransaction(User user, BigDecimal amount, String currency, String type) {
        try {
            BigDecimal amountInINR = currencyConversionService.convertToINR(amount, currency);
    
            if ("credit".equalsIgnoreCase(type)) {
                user.setBalance(user.getBalance().add(amountInINR));
            } else if ("debit".equalsIgnoreCase(type)) {
                user.setBalance(user.getBalance().subtract(amountInINR));
            }
    
            userRepository.save(user);
    
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setAmount(amount);
            transaction.setCurrency(currency);
            transaction.setAmountInINR(amountInINR);
            transaction.setType(type);
            transaction.setTimestamp(LocalDateTime.now());
    
            return transactionRepository.save(transaction);
    
        } catch (Exception e) {
            System.out.println("ERROR in TransactionService: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Transaction failed", e);
        }
    }
    
    
}
