package calculator;

import calculator.command.Command;
import calculator.command.CommandFactory;
import calculator.command.CommandFactoryLoader;
import calculator.command.exceptions.CalculatorException;
import calculator.context.ExecutionContext;
import calculator.util.CalculatorLogger;

import java.io.*;

public class Calculator {
    private final CommandFactory commandFactory;
    private final ExecutionContext context;
    
    public Calculator() {
        this.commandFactory = new CommandFactory();
        this.context = new ExecutionContext();
        CommandFactoryLoader.loadCommands(commandFactory);
    }
    
    public void executeCommand(String line) {
        int commentIndex = line.indexOf('#');
        if (commentIndex >= 0) {
            line = line.substring(0, commentIndex);
        }
        
        line = line.trim();
        if (line.isEmpty()) {
            return;
        }
        
        String[] parts = line.split("\\s+");
        String commandName = parts[0].toUpperCase();
        
        try {
            if (commandFactory.hasCommand(commandName)) {
                Command command = commandFactory.createCommand(commandName);
                command.execute(context, parts);
                CalculatorLogger.info("Executed: " + line);
            } else {
                CalculatorLogger.warning("Unknown command: " + commandName);
                System.err.println("Error: Unknown command: " + commandName);
            }
        } catch (CalculatorException e) {
            CalculatorLogger.severe("Error executing command: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public void executeFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                executeCommand(line);
            }
        }
        CalculatorLogger.info("Executed file: " + filename);
    }
    
    public void executeInteractive() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            System.out.println("Calculator ready. Enter commands (type 'EXIT' to quit):");
            
            while (true) {
                System.out.print("> ");
                line = reader.readLine();
                
                if (line == null || line.trim().equalsIgnoreCase("EXIT")) {
                    break;
                }
                
                executeCommand(line);
            }
        } catch (IOException e) {
            CalculatorLogger.severe("Error reading input: " + e.getMessage());
        }
    }
    
    public ExecutionContext getContext() {
        return context;
    }
}