package com.zqksk.api.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CacheService {

    @CachePut(key = "#employeeNo", cacheNames = "authentication_codes")
    public String generateAuthenticationCode(String employeeNo) {
        return String.format("%04d", new SecureRandom().nextInt(10000));
    }

    @Cacheable(key = "#employeeNo", cacheNames = "authentication_codes")
    public String getAuthenticationCode(String employeeNo) {
        return null;
    }

    @CachePut(key = "#employeeNo", cacheNames = "verification_codes")
    public String generateVerificationCode(String employeeNo) {
        return String.format("%04d", new SecureRandom().nextInt(10000));
    }

    @Cacheable(key = "#employeeNo", cacheNames = "verification_codes")
    public String getVerificationCode(String employeeNo) {
        return null;
    }
}
