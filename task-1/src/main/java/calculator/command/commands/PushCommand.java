package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.*;

@CommandInfo(name = "PUSH", description = "Push a number or parameter onto the stack")
public class PushCommand implements Command {
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (args.length < 2) {
            throw new InvalidArgumentException("PUSH requires an argument");
        }
        
        String arg = args[1];
        double value;
        
        if (context.hasParameter(arg)) {
            value = context.getParameter(arg);
        } else {
            try {
                value = Double.parseDouble(arg);
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException("Invalid number or undefined parameter: " + arg);
            }
        }
        
        context.push(value);
    }
}