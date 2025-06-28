package com.kirana.register.kafka.consumer;

import com.kirana.register.kafka.dto.ReportRequest;
import com.kirana.register.kafka.dto.ReportResult;
import com.kirana.register.kafka.dto.DetailedReportResult;
import com.kirana.register.kafka.dto.DetailedReportRequest;
import com.kirana.register.kafka.dto.TransactionSummary;
import com.kirana.register.model.Transaction;
import com.kirana.register.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ReportConsumer {

    @Autowired
    private TransactionRepository transactionRepository;

    private static final ConcurrentHashMap<String, ReportResult> reportStore = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, DetailedReportResult> detailedReportStore = new ConcurrentHashMap<>();
     
    public static ReportResult getCachedReport(Long userId, String range) {
        return reportStore.get(userId + "-" + range);
    }
    public static DetailedReportResult getCachedDetailedReport(Long userId, String range) {
        return detailedReportStore.get(userId + "-" + range);
    }

    @KafkaListener(topics = "transaction-reports", groupId = "report-consumer-group")
    public void consumeReportRequest(ReportRequest request) {
        System.out.println("Received ReportRequest: userId = " + request.getUserId() + ", range = " + request.getRange());
        System.out.println("***************** consumeReportRequest TRIGGERED with: " + request);

        Long userId = request.getUserId();
        String range = request.getRange();
        LocalDateTime startTime = getStartDateForRange(range);
        LocalDateTime endTime = LocalDateTime.now();

        List<Transaction> transactions = transactionRepository.findByUserIdAndTimestampBetween(userId, startTime, endTime);

        BigDecimal totalCredits = BigDecimal.ZERO;
        BigDecimal totalDebits = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if ("credit".equalsIgnoreCase(t.getType())) {
                totalCredits = totalCredits.add(t.getAmountInINR());
            } else if ("debit".equalsIgnoreCase(t.getType())) {
                totalDebits = totalDebits.add(t.getAmountInINR());
            }
        }

        BigDecimal netFlow = totalCredits.subtract(totalDebits);
        ReportResult result = new ReportResult(userId, range, totalCredits, totalDebits, netFlow);

        reportStore.put(userId + "-" + range, result);
        System.out.println("Report ready for userId=" + userId + ", range=" + range);
    }

    @KafkaListener(topics = "detailed-reports", groupId = "report-consumer-group")
    public void processDetailedReport(DetailedReportRequest request) {
        System.out.println("***************** detailedconsumeReportRequest TRIGGERED with: " + request);
        String range = request.getRange();
        LocalDateTime startTime = getStartDateForRange(range);
        LocalDateTime endTime = LocalDateTime.now();

        List<Transaction> transactions;
        Long userId = request.getUserId();
        boolean isAdmin = request.isAdmin();

        if (isAdmin) {
            transactions = transactionRepository.findByTimestampBetween(startTime, endTime);
        } else {
            transactions = transactionRepository.findByUserIdAndTimestampBetween(userId, startTime, endTime);
        }

        List<TransactionSummary> summaries = transactions.stream()
            .map(tx -> new TransactionSummary(
                tx.getId(),
                tx.getUser().getId(),
                tx.getAmount(),
                tx.getCurrency(),
                tx.getType(),
                tx.getAmountInINR(),
                tx.getTimestamp()
            ))
            .collect(Collectors.toList());
            System.out.println("Summaries: " + summaries);

        DetailedReportResult result = new DetailedReportResult(userId, range, isAdmin, summaries);
        System.out.println("DetailedReportResult: " + result);
        detailedReportStore.put(userId + "-" + range, result);

        System.out.println("Detailed report generated: " + result);
    }

    private LocalDateTime getStartDateForRange(String range) {
        LocalDateTime now = LocalDateTime.now();
        return switch (range.toLowerCase()) {
            case "weekly" -> now.minusWeeks(1);
            case "monthly" -> now.minusMonths(1);
            case "yearly" -> now.minusYears(1);
            default -> now.minusDays(7);
        };
    }
}
