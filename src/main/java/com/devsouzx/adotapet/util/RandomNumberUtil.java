package com.devsouzx.adotapet.util;

import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Locale;

@NoArgsConstructor
public class RandomNumberUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomCode(){
        int randomNumber = RANDOM.nextInt(1_000_000);
        return String.format(Locale.ROOT, "%06d", randomNumber);
    }
}
