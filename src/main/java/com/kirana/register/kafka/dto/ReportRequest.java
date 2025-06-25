package com.kirana.register.kafka.dto;

public class ReportRequest {
    private Long userId;
    private String range; // "weekly", "monthly", "yearly"

    public ReportRequest() {}

    public ReportRequest(Long userId, String range) {
        this.userId = userId;
        this.range = range;
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
}
