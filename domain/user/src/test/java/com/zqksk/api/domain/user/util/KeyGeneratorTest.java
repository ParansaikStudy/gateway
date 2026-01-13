package com.zqksk.api.domain.user.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @Test
    void generateKey() {
        String key = KeyGenerator.generateKey();
        System.out.println(key);
        assertFalse(key.isBlank());

    }
}