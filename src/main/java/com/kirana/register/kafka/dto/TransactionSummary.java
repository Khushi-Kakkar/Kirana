package com.kirana.register.kafka.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionSummary {

    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String currency;
    private String type;
    private BigDecimal amountInINR;
    private LocalDateTime timestamp;

    public TransactionSummary() {}

    public TransactionSummary(Long id, Long userId, BigDecimal amount, String currency,
                               String type, BigDecimal amountInINR, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.amountInINR = amountInINR;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmountInINR() {
        return amountInINR;
    }

    public void setAmountInINR(BigDecimal amountInINR) {
        this.amountInINR = amountInINR;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
