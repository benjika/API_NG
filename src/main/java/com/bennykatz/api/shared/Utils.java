package com.bennykatz.api.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateAddressId(int length) {
        return generateRandomString(length);
    }

    public String generateTeamId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            stringBuilder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(stringBuilder);
    }
}
