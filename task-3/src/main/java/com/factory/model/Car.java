package com.factory.model;

import java.util.concurrent.atomic.AtomicLong;

public class Car {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final long id;
    private final Body body;
    private final Engine engine;
    private final Accessory accessory;

    public Car(Body body, Engine engine, Accessory accessory) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
    }

    public long getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }

    public Engine getEngine() {
        return engine;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    @Override
    public String toString() {
        return String.format("Car #%d (Body: %d, Motor: %d, Accessory: %d)",
                id, body.getId(), engine.getId(), accessory.getId());
    }
}