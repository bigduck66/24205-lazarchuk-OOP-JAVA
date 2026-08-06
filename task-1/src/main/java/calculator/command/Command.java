package calculator.command;

import calculator.context.ExecutionContext;
import calculator.command.exceptions.CalculatorException;

public interface Command {
    void execute(ExecutionContext context, String[] args) throws CalculatorException;
}