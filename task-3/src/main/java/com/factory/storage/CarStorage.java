package com.factory.storage;

import com.factory.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public class CarStorage {
    private static final Logger logger = LoggerFactory.getLogger(CarStorage.class);
    private final Queue<Car> cars;
    private final int capacity;
    private int totalProduced = 0;
    private int totalSold = 0;

    public CarStorage(int capacity) {
        this.capacity = capacity;
        this.cars = new LinkedList<>();
    }

    public synchronized void put(Car car) throws InterruptedException {
        while (cars.size() >= capacity) {
            logger.debug("Car storage is full, waiting...");
            wait();
        }
        cars.add(car);
        totalProduced++;
        logger.info("Car {} added to storage, total produced: {}", car.getId(), totalProduced);
        notifyAll();
    }

    public synchronized Car take() throws InterruptedException {
        while (cars.isEmpty()) {
            logger.debug("Car storage is empty, waiting...");
            wait();
        }
        Car car = cars.poll();
        totalSold++;
        logger.info("Car {} taken from storage, total sold: {}", car.getId(), totalSold);
        notifyAll();
        return car;
    }

    public synchronized int getCurrentSize() {
        return cars.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getTotalProduced() {
        return totalProduced;
    }
    
    public synchronized int getTotalSold() {
        return totalSold;
    }
}