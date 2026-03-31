package calculator.command.exceptions;

public class DivisionByZeroException extends CalculatorException {
    public DivisionByZeroException() {
        super("Division by zero");
    }
}