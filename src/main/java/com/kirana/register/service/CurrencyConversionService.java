package com.kirana.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.Duration;

@Service
public class CurrencyConversionService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private StringRedisTemplate redisTemplate;

    public BigDecimal convertToINR(BigDecimal amount, String fromCurrency) {
        if ("INR".equalsIgnoreCase(fromCurrency)) {
            return amount;
        }

        String key = "fx::" + fromCurrency.toUpperCase();
        String cachedRate = redisTemplate.opsForValue().get(key);

        BigDecimal rate;
        if (cachedRate != null) {
            System.out.println("[FX] Using cached rate for " + fromCurrency);
            rate = new BigDecimal(cachedRate);
        } else {
            System.out.println("[FX] Fetching live rate for " + fromCurrency);
            String apiUrl = "https://api.fxratesapi.com/latest?base=" + fromCurrency;
            String response = restTemplate.getForObject(apiUrl, String.class);

            JSONObject json = new JSONObject(response);
            rate = json.getJSONObject("rates").getBigDecimal("INR");

            redisTemplate.opsForValue().set(key, rate.toPlainString(), Duration.ofMinutes(10));
        }

        return amount.multiply(rate);
    }
}
