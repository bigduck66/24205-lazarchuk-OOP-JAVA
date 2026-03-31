package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandInfo(name = "PRINT", description = "Print the top element of the stack")
public class PrintCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PrintCommand.class);
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (context.getStack().isEmpty()) {
            throw new StackUnderflowException("PRINT");
        }
        
        double value = context.peek();
        System.out.println(value);
        logger.info("Printed: {}", value);
    }
}