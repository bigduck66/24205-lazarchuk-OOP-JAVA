package com.factory;

import com.factory.model.*;
import com.factory.storage.CarStorage;
import com.factory.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FactoryTest {
    private Storage<Body> bodyStorage;
    private Storage<Engine> engineStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;

    @BeforeEach
    void setUp() {
        bodyStorage = new Storage<>(10);
        engineStorage = new Storage<>(10);
        accessoryStorage = new Storage<>(10);
        carStorage = new CarStorage(5);
    }

    @Test
    void testCarAssembly() throws InterruptedException {
        bodyStorage.put(new Body());
        engineStorage.put(new Engine());
        accessoryStorage.put(new Accessory());

        assertEquals(1, bodyStorage.getCurrentSize());
        assertEquals(1, engineStorage.getCurrentSize());
        assertEquals(1, accessoryStorage.getCurrentSize());

        Body body = bodyStorage.take();
        Engine engine = engineStorage.take();
        Accessory accessory = accessoryStorage.take();

        Car car = new Car(body, engine, accessory);
        assertNotNull(car);
        assertTrue(car.getId() > 0);

        carStorage.put(car);
        assertEquals(1, carStorage.getCurrentSize());

        Car takenCar = carStorage.take();
        assertEquals(car.getId(), takenCar.getId());
    }
}