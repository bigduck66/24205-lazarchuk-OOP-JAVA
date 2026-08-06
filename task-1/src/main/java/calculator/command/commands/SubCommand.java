package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.*;

@CommandInfo(name = "-", description = "Subtract top two numbers on the stack")
public class SubCommand implements Command {
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (context.getStack().size() < 2) {
            throw new StackUnderflowException("Subtraction");
        }
        
        double b = context.pop();
        double a = context.pop();
        context.push(a - b);
    }
}