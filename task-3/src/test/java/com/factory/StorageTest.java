package com.factory;

import com.factory.model.Body;
import com.factory.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    private Storage<Body> storage;

    @BeforeEach
    void setUp() {
        storage = new Storage<>(3);
    }

    @Test
    void testPutAndTake() throws InterruptedException {
        Body body = new Body();
        storage.put(body);
        assertEquals(1, storage.getCurrentSize());
        
        Body taken = storage.take();
        assertEquals(body.getId(), taken.getId());
        assertEquals(0, storage.getCurrentSize());
    }

    @Test
    void testCapacityLimit() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            storage.put(new Body());
        }
        assertEquals(3, storage.getCurrentSize());
        
        Thread producer = new Thread(() -> {
            try {
                storage.put(new Body());
                fail("Should have blocked");
            } catch (InterruptedException e) {
            }
        });
        producer.start();
        Thread.sleep(100);
        assertTrue(producer.isAlive());
        producer.interrupt();
    }

    @Test
    void testTotalProduced() throws InterruptedException {
        storage.put(new Body());
        storage.put(new Body());
        assertEquals(2, storage.getTotalProduced());
        
        storage.take();
        storage.put(new Body());
        assertEquals(3, storage.getTotalProduced());
    }
}