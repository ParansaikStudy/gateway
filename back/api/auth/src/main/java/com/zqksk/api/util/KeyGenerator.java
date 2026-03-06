package com.zqksk.api.util;

import com.fasterxml.uuid.Generators;

import java.util.Random;

public class KeyGenerator {

    private KeyGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateKey() {
        return generateUUID();
    }

    private static String generateUUID() {
        return Generators.timeBasedEpochRandomGenerator(new Random()).generate().toString().replace("-", "");
    }
}
