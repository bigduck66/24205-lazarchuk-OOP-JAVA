package com.factory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    private final Properties properties;

    public Configuration(String configFile) throws IOException {
        properties = new Properties();//Map<String, String>
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
            logger.info("Configuration loaded from {}", configFile);
        }
    }

    public int getStorageBodySize() {
        return Integer.parseInt(properties.getProperty("StorageBodySize", "100"));
    }

    public int getStorageMotorSize() {
        return Integer.parseInt(properties.getProperty("StorageMotorSize", "100"));
    }

    public int getStorageAccessorySize() {
        return Integer.parseInt(properties.getProperty("StorageAccessorySize", "100"));
    }

    public int getStorageAutoSize() {
        return Integer.parseInt(properties.getProperty("StorageAutoSize", "50"));
    }

    public int getAccessorySuppliers() {
        return Integer.parseInt(properties.getProperty("AccessorySuppliers", "5"));
    }

    public int getBodySuppliers() {
        return Integer.parseInt(properties.getProperty("BodySuppliers", "3"));
    }

    public int getMotorSuppliers() {
        return Integer.parseInt(properties.getProperty("MotorSuppliers", "4"));
    }

    public int getWorkers() {
        return Integer.parseInt(properties.getProperty("Workers", "10"));
    }

    public int getDealers() {
        return Integer.parseInt(properties.getProperty("Dealers", "20"));
    }

    public int getSupplierDelay() {
        return Integer.parseInt(properties.getProperty("SupplierDelay", "1000"));
    }

    public int getDealerDelay() {
        return Integer.parseInt(properties.getProperty("DealerDelay", "2000"));
    }

    public boolean isLogSale() {
        return Boolean.parseBoolean(properties.getProperty("LogSale", "true"));
    }
}