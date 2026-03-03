package com.game;
//добавить task0 папку
public class BullsAndCowsCounter {
    
    public static CountResult count(int hiddenNumber, int estimatedNumber) {
        String hidden = String.valueOf(hiddenNumber);
        String estimated = String.valueOf(estimatedNumber);
        
        int bulls = countBulls(hidden, estimated);
        int cows = countCows(hidden, estimated);
        
        return new CountResult(bulls, cows);
    }
    
    private static int countBulls(String hidden, String estimated) {
        int bulls = 0;
        for (int i = 0; i < hidden.length(); ++i) {
            if (hidden.charAt(i) == estimated.charAt(i)) {
                ++bulls;
            }
        }
        return bulls;
    }
    
    private static int countCows(String hidden, String estimated) {
        int cows = 0;
        boolean[] matchedInEstimated = new boolean[estimated.length()];
        
        for (int i = 0; i < hidden.length(); ++i) {
            if (hidden.charAt(i) == estimated.charAt(i)) {
                matchedInEstimated[i] = true;
            }
        }
        
        for (int i = 0; i < hidden.length(); ++i) {
            if (hidden.charAt(i) == estimated.charAt(i)) continue;
            
            for (int j = 0; j < estimated.length(); ++j) {
                if (!matchedInEstimated[j] && hidden.charAt(i) == estimated.charAt(j)) {
                    ++cows;
                    matchedInEstimated[j] = true;
                    break;
                }
            }
        }
        
        return cows;
    }
}