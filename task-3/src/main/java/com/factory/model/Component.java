package com.factory.model;

import java.util.concurrent.atomic.AtomicLong;

public abstract class Component {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final long id;
    private final String type;

    public Component(String type) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " #" + id;
    }
}