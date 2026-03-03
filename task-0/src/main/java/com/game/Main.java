package com.game;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameLogger logger = new GameLogger();
        Scanner scanner = new Scanner(System.in);
        
        try {
            int hiddenNumber = RandomNumberGenerator.generateUnique4DigitNumber();
            int estimatedNumber = 0;
            
            logger.logGameStart(hiddenNumber);

            while (hiddenNumber != estimatedNumber) {
                
                try{
                    estimatedNumber = scanner.nextInt();
                    
                    if (estimatedNumber < 1000 || estimatedNumber > 9999){
                        System.out.println("only 4-digit number");
                        logger.debug("player entered non-4-digit number: {}", estimatedNumber);
                        continue;
                    }
                    
                    logger.logPlayerGuess(estimatedNumber);
                    
                    CountResult result = BullsAndCowsCounter.count(hiddenNumber, estimatedNumber);
                    
                    System.out.println("bulls: " + result.getBulls());
                    System.out.println("cows: " + result.getCows());
                    
                    logger.logResult(result.getBulls(), result.getCows());
                    
                } catch (InputMismatchException e){
                    String invalidInput = scanner.next();
                    System.out.println("only 4-digit number");
                    logger.debug("player entered invalid input: '{}'", invalidInput);
                }
            }
            
            System.out.println("win");
            logger.logGameEnd(true, hiddenNumber);
            
        } finally {
            scanner.close();
            logger.close();
        }
    }
}