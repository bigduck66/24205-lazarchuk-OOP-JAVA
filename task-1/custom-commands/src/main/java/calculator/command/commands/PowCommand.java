package calculator.command.commands;

import calculator.command.Command;
import calculator.command.CommandInfo;
import calculator.context.ExecutionContext;
import calculator.command.exceptions.CalculatorException;
import calculator.command.exceptions.StackUnderflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandInfo(name = "POW", description = "Возведение в степень: a^b")
public class PowCommand implements Command {
    
    private static final Logger logger = LoggerFactory.getLogger(PowCommand.class);
    
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (context.getStack().size() < 2) {
            throw new StackUnderflowException("POW");
        }
        
        double exponent = context.pop();
        double base = context.pop();
        double result = Math.pow(base, exponent);
        context.push(result);
        
        logger.info("POW: {} ^ {} = {}", base, exponent, result);
    }
}
