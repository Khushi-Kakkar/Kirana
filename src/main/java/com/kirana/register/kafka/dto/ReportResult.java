package com.kirana.register.kafka.dto;

import java.math.BigDecimal;
// import java.util.*;

/**
 * DTO representing the result of a report generation.
 * 
 */

public class ReportResult {
    private Long userId;
    private String range;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private BigDecimal netFlow;
    // private List <TransactionSummary> transactions;

    public ReportResult() {}

    public ReportResult(Long userId, String range,
                        BigDecimal totalCredits,
                        BigDecimal totalDebits,
                        BigDecimal netFlow) {
        this.userId = userId;
        this.range = range;
        this.totalCredits = totalCredits;
        this.totalDebits = totalDebits;
        this.netFlow = netFlow;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getRange() {
        return range;
    }
    public void setRange(String range) {
        this.range = range;
    }
    public BigDecimal getTotalCredits() {
        return totalCredits;
    }
    public void setTotalCredits(BigDecimal totalCredits) {
        this.totalCredits = totalCredits;
    }
    public BigDecimal getTotalDebits() {
        return totalDebits;
    }
    public void setTotalDebits(BigDecimal totalDebits) {
        this.totalDebits = totalDebits;
    }
    public BigDecimal getNetFlow() {
        return netFlow;
    }
    public void setNetFlow(BigDecimal netFlow) {
        this.netFlow = netFlow;
    }
    // public List <TransactionSummary> getTransactions () {
    //     return transactions;
    // }
    // public void setTransactions(List<TransactionSummary> transactions) {
    //     this.transactions = transactions;
    // }
}
