package calculator.command.exceptions;

public class InvalidArgumentException extends CalculatorException {
    public InvalidArgumentException(String message) {
        super("Invalid argument: " + message);
    }
}