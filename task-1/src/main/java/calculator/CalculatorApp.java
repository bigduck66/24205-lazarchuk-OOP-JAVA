package calculator;

import java.io.IOException;

public class CalculatorApp {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        
        try {
            if (args.length > 0) {
                calculator.executeFile(args[0]);
            } else {
                calculator.executeInteractive();
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}