package calculator.command;

import calculator.command.exceptions.CommandNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Class<? extends Command>> commands = new HashMap<>();
    
    public void registerCommand(String name, Class<? extends Command> commandClass) {
        commands.put(name.toUpperCase(), commandClass);
    }
    
    public Command createCommand(String name) throws CommandNotFoundException {
        Class<? extends Command> commandClass = commands.get(name.toUpperCase());
        
        if (commandClass == null) {
            throw new CommandNotFoundException(name);
        }
        
        try {
            return commandClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | 
                 NoSuchMethodException | InvocationTargetException e) {
            throw new CommandNotFoundException("Failed to instantiate command: " + name);
        }
    }
    
    public boolean hasCommand(String name) {
        return commands.containsKey(name.toUpperCase());
    }
}