package com.factory;

import com.factory.config.Configuration;
import com.factory.controller.FactoryController;
import com.factory.dealer.Dealer;
import com.factory.model.*;
import com.factory.pool.ThreadPool;
import com.factory.storage.CarStorage;
import com.factory.storage.Storage;
import com.factory.supplier.Supplier;
import com.factory.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FactoryManager {
    private static final Logger logger = LoggerFactory.getLogger(FactoryManager.class);

    private final Configuration config;
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final CarStorage carStorage;

    private final List<Supplier<Body>> bodySuppliers = new ArrayList<>();
    private final List<Supplier<Engine>> engineSuppliers = new ArrayList<>();
    private final List<Supplier<Accessory>> accessorySuppliers = new ArrayList<>();
    private final List<Dealer> dealers = new ArrayList<>();
    private ThreadPool workerPool;
    private FactoryController controller;
    private volatile boolean running = false;

    public FactoryManager(Configuration config) {
        this.config = config;

        this.bodyStorage = new Storage<>(config.getStorageBodySize());
        this.engineStorage = new Storage<>(config.getStorageMotorSize());
        this.accessoryStorage = new Storage<>(config.getStorageAccessorySize());
        this.carStorage = new CarStorage(config.getStorageAutoSize());

        logger.info("FactoryManager created");
    }

    public void initFactory() {
        createSuppliers();
        createWorkerPool();
        createController();
        createDealers();
        logger.info("Factory initialized");
    }

    private void createSuppliers() {
        for (int i = 0; i < config.getBodySuppliers(); i++) {
            Supplier<Body> supplier = new Supplier<>("BodySupplier-" + i, bodyStorage, Body::new);
            supplier.setDelay(config.getSupplierDelay());
            bodySuppliers.add(supplier);
        }

        for (int i = 0; i < config.getMotorSuppliers(); i++) {
            Supplier<Engine> supplier = new Supplier<>("EngineSupplier-" + i, engineStorage, Engine::new);
            supplier.setDelay(config.getSupplierDelay());
            engineSuppliers.add(supplier);
        }

        for (int i = 0; i < config.getAccessorySuppliers(); i++) {
            Supplier<Accessory> supplier = new Supplier<>("AccessorySupplier-" + i, accessoryStorage, Accessory::new);
            supplier.setDelay(config.getSupplierDelay());
            accessorySuppliers.add(supplier);
        }
    }

    private void createWorkerPool() {
        workerPool = new ThreadPool(config.getWorkers());
    }

    private void createController() {
        Runnable assemblyTask = new Runnable() {
            @Override
            public void run(){
                Worker worker = new Worker(0, bodyStorage, engineStorage, accessoryStorage, carStorage);
                worker.run();
            }
        };
        controller = new FactoryController(carStorage, workerPool, assemblyTask);
    }

    private void createDealers() {
        for (int i = 0; i < config.getDealers(); i++) {
            Dealer dealer = new Dealer(i + 1, carStorage, config.isLogSale());
            dealer.setDelay(config.getDealerDelay());
            dealers.add(dealer);
        }
    }

    public void startFactory() {
        if (running) return;
        logger.info("Starting factory");
        running = true;

        bodySuppliers.forEach(Supplier::start);
        engineSuppliers.forEach(Supplier::start);
        accessorySuppliers.forEach(Supplier::start);
        controller.start();
        dealers.forEach(Dealer::start);
    }

    public void stopFactory() {
        if (!running) return;
        logger.info("Stopping factory");
        running = false;

        bodySuppliers.forEach(Supplier::stopSupplier);
        engineSuppliers.forEach(Supplier::stopSupplier);
        accessorySuppliers.forEach(Supplier::stopSupplier);
        controller.stopController();
        dealers.forEach(Dealer::stopDealer);
        workerPool.shutdown();
    }

    public Storage<Body> getBodyStorage() { return bodyStorage; }
    public Storage<Engine> getEngineStorage() { return engineStorage; }
    public Storage<Accessory> getAccessoryStorage() { return accessoryStorage; }
    public CarStorage getCarStorage() { return carStorage; }
    public ThreadPool getWorkerPool() { return workerPool; }
    public List<Supplier<Body>> getBodySuppliers() { return bodySuppliers; }
    public List<Supplier<Engine>> getEngineSuppliers() { return engineSuppliers; }
    public List<Supplier<Accessory>> getAccessorySuppliers() { return accessorySuppliers; }
    public List<Dealer> getDealers() { return dealers; }
}