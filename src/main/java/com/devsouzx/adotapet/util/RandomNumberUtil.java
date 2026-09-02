package com.devsouzx.adotapet.util;

import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor
public class RandomNumberUtil {
    public static String generateRandomCode(){
        Random rnd = new Random();
        int randomNumber = rnd.nextInt(999999);
        return String.format("%06d", randomNumber);
    }
}
