package com.kirana.register.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final ConcurrentHashMap<Long, List<Instant>> userRequestMap = new ConcurrentHashMap<>();

    private static final int LIMIT = 10;
    private static final int WINDOW_SECONDS = 60; 

    public boolean isAllowed(Long userId) {
        Instant now = Instant.now();
        List<Instant> requestTimes = userRequestMap.getOrDefault(userId, new ArrayList<>());

        requestTimes.removeIf(time -> time.isBefore(now.minusSeconds(WINDOW_SECONDS)));

        if (requestTimes.size() >= LIMIT) {
            return false; 
        }

        requestTimes.add(now);
        userRequestMap.put(userId, requestTimes);
        return true;
    }
}
