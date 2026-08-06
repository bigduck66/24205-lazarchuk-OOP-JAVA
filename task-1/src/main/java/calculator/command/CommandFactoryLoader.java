package calculator.command;

import calculator.command.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommandFactoryLoader {
    private static final Logger logger = LoggerFactory.getLogger(CommandFactoryLoader.class);
    
    public static void loadCommands(CommandFactory factory) {
        loadDefaultCommands(factory);
        loadCommandsFromConfig(factory);
    }
    
    private static void loadCommandsFromConfig(CommandFactory factory) {
        try {
            InputStream configStream = CommandFactoryLoader.class
                .getResourceAsStream("/commands.config");
            
            if (configStream == null) {
                logger.warn("Commands config file not found");
                return;
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(configStream));
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                loadCommandsFromJar(factory, line);
            }
            
            reader.close();
            
        } catch (IOException e) {
            logger.error("Failed to load commands config: {}", e.getMessage());
        }
    }
    
    private static void loadCommandsFromJar(CommandFactory factory, String jarPath) {
        try {
            File jarFile = new File(jarPath);
            if (!jarFile.exists()) {
                logger.warn("JAR file not found: {}", jarPath);
                return;
            }
            
            URL jarUrl = jarFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl});
            
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                
                if (entryName.endsWith(".class")) {
                    String className = entryName.replace("/", ".")
                        .replace(".class", "");
                    
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        
                        if (Command.class.isAssignableFrom(clazz) && 
                            clazz.isAnnotationPresent(CommandInfo.class)) {
                            CommandInfo info = clazz.getAnnotation(CommandInfo.class);
                            factory.registerCommand(info.name(), 
                                (Class<? extends Command>) clazz);
                            logger.info("Loaded command: {} from {}", info.name(), jarPath);
                        }
                    } catch (ClassNotFoundException e) {
                        logger.warn("Failed to load class: {}", className);
                    }
                }
            }
            
            jar.close();
            
        } catch (IOException e) {
            logger.error("Failed to load JAR: {}", jarPath);
        }
    }
    
    private static void loadDefaultCommands(CommandFactory factory) {
        factory.registerCommand("PUSH", PushCommand.class);
        factory.registerCommand("POP", PopCommand.class);
        factory.registerCommand("+", AddCommand.class);
        factory.registerCommand("-", SubCommand.class);
        factory.registerCommand("*", MulCommand.class);
        factory.registerCommand("/", DivCommand.class);
        factory.registerCommand("SQRT", SqrtCommand.class);
        factory.registerCommand("PRINT", PrintCommand.class);
        factory.registerCommand("DEFINE", DefineCommand.class);
        
        logger.info("Loaded default commands");
    }
}