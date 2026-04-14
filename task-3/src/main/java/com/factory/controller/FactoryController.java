package com.factory.controller;

import com.factory.storage.CarStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class FactoryController extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(FactoryController.class);
    private final CarStorage carStorage;
    private final ExecutorService workerPool;
    private final Runnable taskFactory;
    private volatile boolean running = true;

    public FactoryController(CarStorage carStorage, ExecutorService workerPool, 
                            Runnable taskFactory) {
        super("FactoryController");
        this.carStorage = carStorage;
        this.workerPool = workerPool;
        this.taskFactory = taskFactory;
        logger.info("Factory controller created");
    }

    public void stopController() {
        running = false;
        logger.info("Stopping factory controller");
        interrupt();
    }

    @Override
    public void run() {
        logger.info("Factory controller started");
        while (running) {
            synchronized (carStorage) {
                try {
                    while (carStorage.getCurrentSize() >= carStorage.getCapacity() / 2) {
                        logger.debug("Car storage is above 50%, waiting...");
                        carStorage.wait();
                    }
                    
                    int carsToBuild = carStorage.getCapacity() - carStorage.getCurrentSize();
                    logger.info("Creating {} assembly tasks", carsToBuild);
                    
                    for (int i = 0; i < carsToBuild; i++) {
                        workerPool.submit(taskFactory);
                    }
                    
                    carStorage.wait();
                } catch (InterruptedException e) {
                    logger.info("Factory controller interrupted");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        logger.info("Factory controller stopped");
    }
}