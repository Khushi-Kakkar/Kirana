package com.kirana.register.kafka.consumer;

import com.kirana.register.kafka.dto.ReportRequest;
import com.kirana.register.kafka.dto.ReportResult;
import com.kirana.register.model.Transaction;
import com.kirana.register.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReportConsumer {

    @Autowired
    private TransactionRepository transactionRepository;

    // Temporary in-memory store
    private static final ConcurrentHashMap<String, ReportResult> reportStore = new ConcurrentHashMap<>();

    public static ReportResult getCachedReport(Long userId, String range) {
        return reportStore.get(userId + "-" + range);
    }

    @KafkaListener(topics = "transaction-reports", groupId = "report-consumer-group")
    public void consumeReportRequest(ReportRequest request) {
        System.out.println("Received ReportRequest: userId = " + request.getUserId() + ", range = " + request.getRange());

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
