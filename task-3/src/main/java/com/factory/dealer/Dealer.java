package com.factory.dealer;

import com.factory.model.Car;
import com.factory.storage.CarStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dealer extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Dealer.class);
    private static final Logger saleLogger = LoggerFactory.getLogger("main.java.com.factory.SaleLogger");
    
    private final CarStorage carStorage;
    private final int id;
    private final boolean loggingEnabled;
    private volatile boolean running = true;
    private volatile int delay = 2000;

    public Dealer(int id, CarStorage carStorage, boolean loggingEnabled) {
        super("Dealer-" + id);
        this.id = id;
        this.carStorage = carStorage;
        this.loggingEnabled = loggingEnabled;
        logger.info("Created dealer {}", id);
    }

    public void setDelay(int delay) {
        this.delay = delay;
        logger.debug("Dealer {} delay set to {}ms", id, delay);
    }

    public void stopDealer() {
        running = false;
        logger.info("Stopping dealer {}", id);
        interrupt();
    }

    private void logSale(Car car) {
        if (!loggingEnabled) return;
        
        String logEntry = String.format("Dealer %d: Auto %d (Body: %d, Motor: %d, Accessory: %d)",
                id, car.getId(), car.getBody().getId(), 
                car.getEngine().getId(), car.getAccessory().getId());
        saleLogger.info(logEntry);
    }

    @Override
    public void run() {
        logger.info("Dealer {} started", id);
        while (running) {
            try {
                Thread.sleep(delay);
                logger.debug("Dealer {} requesting car", id);
                Car car = carStorage.take();
                logSale(car);
                logger.info("Dealer {} bought {}", id, car);
            } catch (InterruptedException e) {
                logger.info("Dealer {} interrupted", id);
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Dealer {} stopped", id);
    }
}