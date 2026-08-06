package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.*;

@CommandInfo(name = "DEFINE", description = "Define a parameter with a value")
public class DefineCommand implements Command {
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (args.length < 3) {
            throw new InvalidArgumentException("DEFINE requires a name and a value");
        }
        
        String paramName = args[1];
        double value;
        
        try {
            value = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid number format: " + args[2]);
        }
        
        context.define(paramName, value);
    }
}