package calculator.command.exceptions;

public class ParameterNotFoundException extends CalculatorException {
    public ParameterNotFoundException(String paramName) {
        super("Parameter not found: " + paramName);
    }
}