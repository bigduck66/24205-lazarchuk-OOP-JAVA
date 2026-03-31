package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.*;

@CommandInfo(name = "/", description = "Divide top two numbers on the stack")
public class DivCommand implements Command {
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (context.getStack().size() < 2) {
            throw new StackUnderflowException("Division");
        }
        
        double b = context.pop();
        double a = context.pop();
        
        if (b == 0) {
            context.push(a);
            context.push(b);
            throw new DivisionByZeroException();
        }
        
        context.push(a / b);
    }
}