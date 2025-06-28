package com.kirana.register.kafka.producer;

import com.kirana.register.kafka.KafkaTopics;
import com.kirana.register.kafka.dto.ReportRequest;
import com.kirana.register.kafka.dto.DetailedReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class ReportProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Send Summary Report Request
     * @param request
     */
    public void sendReportRequest(ReportRequest request) {
        kafkaTemplate.send(KafkaTopics.REPORT_TOPIC, request);
    }
    /**
     * Send Detailed Report
     * @param request
     */
    public void sendDetailedReport(DetailedReportRequest request) {
        kafkaTemplate.send(KafkaTopics.DETAILED_REPORT_TOPIC, request);
    }
}
