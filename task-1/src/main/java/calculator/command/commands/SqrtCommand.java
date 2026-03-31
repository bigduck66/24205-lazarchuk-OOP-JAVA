package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.*;

@CommandInfo(name = "SQRT", description = "Calculate square root of top element")
public class SqrtCommand implements Command {
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (context.getStack().isEmpty()) {
            throw new StackUnderflowException("Square root");
        }
        
        double a = context.pop();
        
        if (a < 0) {
            context.push(a);
            throw new InvalidArgumentException("Cannot take square root of negative number: " + a);
        }
        
        context.push(Math.sqrt(a));
    }
}