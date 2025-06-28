package com.kirana.register.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.kirana.register.kafka.consumer.ReportConsumer;
import com.kirana.register.kafka.dto.DetailedReportResult;
import com.kirana.register.kafka.dto.ReportRequest;
import com.kirana.register.kafka.dto.ReportResult;
import com.kirana.register.kafka.producer.ReportProducer;
import com.kirana.register.model.User;
import com.kirana.register.repository.UserRepository;
import com.kirana.register.kafka.dto.DetailedReportRequest;



@RestController
@RequestMapping ("/report")

public class ReportController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportProducer reportProducer;

    @PostMapping ("/trigger")
    public ResponseEntity<String> triggerReport(@RequestParam String range) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        ReportRequest request = new ReportRequest(user.getId(), range.toLowerCase());
        reportProducer.sendReportRequest(request);

        return ResponseEntity.accepted().body("Report request submitted for processing.");
    }
    
    @GetMapping ("/basic")
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
    @PostMapping ("/trigger-detail")
    public ResponseEntity<String> triggerReportDetail(@RequestParam String range) {
        System.out.println(" Trigger report detail");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found.");
        }
        boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole());
        String reportRange = range.toLowerCase();
        if (!reportRange.equals("weekly") && !reportRange.equals("monthly") && !reportRange.equals("yearly")) {
            return ResponseEntity.badRequest().body("Invalid range. Use 'weekly', 'monthly', or 'yearly'.");
        }

        DetailedReportRequest request = new DetailedReportRequest(user.getId(), reportRange, isAdmin);
        reportProducer.sendDetailedReport(request);

        return ResponseEntity.accepted().body("Report request for " + reportRange + " submitted.");
    }
    @GetMapping("/detailed")
    public ResponseEntity<?> getDetailedReport(@RequestParam String range) {
        System.out.println(" Get detailed report");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
    
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found.");
        }
        
        String reportRange = range.toLowerCase();
        DetailedReportResult result = ReportConsumer.getCachedDetailedReport(user.getId(), reportRange);
        System.out.println("DetailedReportResult: " + result);
        if (result == null) {
            return ResponseEntity.status(202).body("Detailed report is still being generated. Please try again shortly.");
        }
    
        return ResponseEntity.ok(result);
    }
        
}
