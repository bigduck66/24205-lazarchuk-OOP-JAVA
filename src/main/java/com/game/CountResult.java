package com.game;

public class CountResult {
    private final int bulls;
    private final int cows;
    
    public CountResult(int bulls, int cows) {
        this.bulls = bulls;
        this.cows = cows;
    }
    
    public int getBulls() {
        return bulls;
    }
    
    public int getCows() {
        return cows;
    }
    
    @Override
    public String toString() {
        return "Bulls: " + bulls + ", Cows: " + cows;
    }
}