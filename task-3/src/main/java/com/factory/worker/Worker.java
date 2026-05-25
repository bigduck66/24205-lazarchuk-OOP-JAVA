package com.factory.worker;

import com.factory.model.*;
import com.factory.storage.CarStorage;
import com.factory.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final CarStorage carStorage;
    private final int id;
    private static volatile int carsBuilt = 0;

    public Worker(int id, Storage<Body> bodyStorage, Storage<Engine> engineStorage,
                  Storage<Accessory> accessoryStorage, CarStorage carStorage) {
        this.id = id;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
    }

    public static synchronized int getCarsBuilt() {
        return carsBuilt;
    }

    private static synchronized void incrementCarsBuilt() {
        carsBuilt++;
    }

    @Override
    public void run() {
        logger.debug("Worker {} started assembling car", id);
        try {
            Body body = bodyStorage.take();
            logger.debug("Worker {} took {}", id, body);
            
            Engine engine = engineStorage.take();
            logger.debug("Worker {} took {}", id, engine);
            
            Accessory accessory = accessoryStorage.take();
            logger.debug("Worker {} took {}", id, accessory);

            Car car = new Car(body, engine, accessory);
            carStorage.put(car);
            incrementCarsBuilt();
            
            logger.info("Worker {} assembled {}", id, car);
        } catch (InterruptedException e) {
            logger.error("Worker {} was interrupted", id);
            Thread.currentThread().interrupt();
        }
    }

    public int getId() {
        return id;
    }
    // public void nothing() {
    //     Thread task = new Thread(new Worker(1, null, null, null, null));
    // }
}