package com.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomNumberGenerator {
    public static int generateUnique4DigitNumber() {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            digits.add(i);
        }
        
        Collections.shuffle(digits);
        
        if (digits.get(0) == 0) {
            Collections.swap(digits, 0, 1);
        }
        
        return digits.get(0) * 1000 + 
               digits.get(1) * 100 + 
               digits.get(2) * 10 + 
               digits.get(3);
    }
}