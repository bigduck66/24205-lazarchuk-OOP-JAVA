package calculator.command.exceptions;

public class StackUnderflowException extends CalculatorException {
    public StackUnderflowException(String operation) {
        super("Stack underflow during operation: " + operation);
    }
}