package com.kirana.register.kafka.dto;


public class DetailedReportRequest {
    private Long userId;
    private String range; 
    private boolean isAdmin;

    public DetailedReportRequest() {}

    public DetailedReportRequest(Long userId, String range, boolean isAdmin) {
        this.userId = userId;
        this.range = range;
        this.isAdmin = isAdmin;
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
}


