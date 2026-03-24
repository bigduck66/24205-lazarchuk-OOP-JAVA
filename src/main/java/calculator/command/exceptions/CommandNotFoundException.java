package calculator.command.exceptions;

public class CommandNotFoundException extends CalculatorException {
    public CommandNotFoundException(String commandName) {
        super("Command not found: " + commandName);
    }
}