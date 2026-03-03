package com.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLogger {
    private static final Logger logger = LoggerFactory.getLogger(GameLogger.class);
    
    public GameLogger() {
        try {
            logger.info("new game");
        } catch (Exception e) {
            System.err.println("can't create file" + e.getMessage());
        }
    }
    
    public void log(String message) {
        logger.info(message);
    }
    
    public void logGameStart(int hiddenNumber) {
        logger.info("game start guess number: {}", hiddenNumber);
    }
    
    public void logPlayerGuess(int guess) {
        logger.info("the player entered a number: {}", guess);
    }
    
    public void logInvalidInput(String input) {
        logger.info("error: The player entered an invalid value: '{}'", input);
    }
    
    public void logResult(int bulls, int cows) {
        logger.info("result: bulls {}, cows {}", bulls, cows);
    }
    
    public void logGameEnd(boolean won, int hiddenNumber) {
        if (won) {
            logger.info("win hidden number: {}", hiddenNumber);
        } else {
            logger.info("game end hidden number: {}", hiddenNumber);
        }
        logger.info("game end");
    }
    
    public void debug(String message, int value) {
        logger.debug(message, value);
    }
    
    public void debug(String message, String value) {
        logger.debug(message, value);
    }
    
    public void debug(String message) {
        logger.debug(message);
    }
    
    public void close() {
    }
}