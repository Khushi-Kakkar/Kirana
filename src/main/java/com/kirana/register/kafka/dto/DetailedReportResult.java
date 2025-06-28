
package com.kirana.register.kafka.dto;

import java.util.*;

/**
 * DTO representing the result of a report generation.
 * 
 */

public class DetailedReportResult {
    private Long userId;
    private String range;
    private boolean isAdmin;
    private List <TransactionSummary> transactions;

    public DetailedReportResult() {}

    public DetailedReportResult(Long userId, String range, boolean isAdmin, List < TransactionSummary> transactions) {
        this.userId = userId;
        this.range = range;
        this.isAdmin = isAdmin;
        this.transactions = transactions;
       
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
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public List <TransactionSummary> getTransactions () {
        return transactions;
    }
    public void setTransactions(List<TransactionSummary> transactions) {
        this.transactions = transactions;
    }
    
}
