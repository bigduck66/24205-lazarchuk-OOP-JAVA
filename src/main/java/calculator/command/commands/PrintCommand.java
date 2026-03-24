package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.*;
import calculator.util.CalculatorLogger;

@CommandInfo(name = "PRINT", description = "Print the top element of the stack")
public class PrintCommand implements Command {
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (context.getStack().isEmpty()) {
            throw new StackUnderflowException("PRINT");
        }
        
        double value = context.peek();
        System.out.println(value);
        CalculatorLogger.info("Printed: " + value);
    }
}