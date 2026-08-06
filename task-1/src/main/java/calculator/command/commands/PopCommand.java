package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.*;

@CommandInfo(name = "POP", description = "Pop the top element from the stack")
public class PopCommand implements Command {
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        try {
            context.pop();
        } catch (java.util.EmptyStackException e) {
            throw new StackUnderflowException("POP");
        }
    }
}