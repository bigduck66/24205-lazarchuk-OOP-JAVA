package com.game;

public class CheckingMatches {
    private int countBulls;
    private int countCows;

    public CheckingMatches() {
        this.countBulls = 0;
        this.countCows = 0;
    }

    public void count(int hiddenNumber, int estimatedNumber){
        this.countBulls = 0;
        this.countCows = 0;

        String hidden = String.valueOf(hiddenNumber);
        String estimated = String.valueOf(estimatedNumber);

        boolean[] matchedInEstimated = new boolean[estimated.length()];

        for (int i = 0; i < hidden.length(); ++i) {
            if (hidden.charAt(i) == estimated.charAt(i)) {
                this.countBulls++;
                matchedInEstimated[i] = true;
            }
        }

        for (int i = 0; i < hidden.length(); ++i) {
            if (hidden.charAt(i) == estimated.charAt(i)) continue;
            
            for (int j = 0; j < estimated.length(); ++j) {
                if (!matchedInEstimated[j] && hidden.charAt(i) == estimated.charAt(j)) {
                    this.countCows++;
                    matchedInEstimated[j] = true;
                    break;
                }
            }
        }
    }
    
    public int getBulls() {
        return this.countBulls;
    }
    
    public int getCows() {
        return this.countCows;
    }
}