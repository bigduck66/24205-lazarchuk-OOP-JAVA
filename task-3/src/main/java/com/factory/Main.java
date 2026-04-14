package com.factory;

import com.factory.config.Configuration;
import com.factory.ui.FactoryUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        logger.info("Starting Car Factory Emulator");
        SwingUtilities.invokeLater(() -> {
            try {
                Configuration config = new Configuration("config.properties");
                FactoryUI ui = new FactoryUI(config);
                ui.setVisible(true);
                logger.info("Application started successfully");
            } catch (Exception e) {
                logger.error("Error starting application", e);
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}