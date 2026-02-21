package com.game;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameLogger {
    private static final String LOG_FILE = "game.log";
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private PrintWriter writer;
    
    public GameLogger() {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true), true);
            log("new game");
        } catch (IOException e) {
            System.err.println("can't create file" + e.getMessage());
        }
    }
    
    public void log(String message) {
        if (writer != null) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.println(timestamp + " - " + message);
        }
    }
    
    public void logGameStart(int hiddenNumber) {
        log("game start guess number: " + hiddenNumber);
    }
    
    public void logPlayerGuess(int guess) {
        log("the player entered a number: " + guess);
    }
    
    public void logInvalidInput(String input) {
        log("error: The player entered an invalid value: '" + input + "'");
    }
    
    public void logResult(int bulls, int cows) {
        log("result: bulls" + bulls + ", cows" + cows);
    }
    
    public void logGameEnd(boolean won, int hiddenNumber) {
        if (won) {
            log("win hidden number: " + hiddenNumber);
        } else {
            log("game end hidden number: " + hiddenNumber);
        }
        log("game end\n");
    }
    
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}