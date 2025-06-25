package com.kirana.register.kafka.producer;

import com.kirana.register.kafka.KafkaTopics;
import com.kirana.register.kafka.dto.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendReportRequest(ReportRequest request) {
        System.out.println("\uD83D\uDCE4 Sending report request for userId = " + request.getUserId() + ", range = " + request.getRange());
        kafkaTemplate.send(KafkaTopics.REPORT_TOPIC, request);
    }
}
