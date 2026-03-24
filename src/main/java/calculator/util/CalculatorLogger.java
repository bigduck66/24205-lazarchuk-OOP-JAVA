package calculator.util;

import java.io.IOException;
import java.util.logging.*;

public class CalculatorLogger {
    private static final Logger logger = Logger.getLogger("CalculatorLogger");
    private static FileHandler fileHandler;
    
    static {
        try {
            fileHandler = new FileHandler("calculator.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }
    
    public static Logger getLogger() {
        return logger;
    }
    
    public static void info(String message) {
        logger.info(message);
    }
    
    public static void warning(String message) {
        logger.warning(message);
    }
    
    public static void severe(String message) {
        logger.severe(message);
    }
}