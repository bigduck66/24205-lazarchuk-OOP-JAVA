package com.factory.storage;

import com.factory.model.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public class Storage<T extends Component> {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);
    private final Queue<T> items;
    private final int capacity;
    private int totalProduced = 0;

    public Storage(int capacity) {
        this.capacity = capacity;
        this.items = new LinkedList<>();
    }

    public synchronized void put(T item) throws InterruptedException {
        while (items.size() >= capacity) {
            logger.debug("Storage {} is full, waiting...", item.getType());
            wait();
        }
        items.add(item);
        totalProduced++;
        logger.debug("Added {} to storage, current size: {}/{}", 
            item, items.size(), capacity);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (items.isEmpty()) {
            logger.debug("Storage is empty, waiting...");
            wait();
        }
        T item = items.poll();
        logger.debug("Took {} from storage, current size: {}/{}", 
            item, items.size(), capacity);
        notifyAll();
        return item;
    }

    public synchronized int getCurrentSize() {
        return items.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getTotalProduced() {
        return totalProduced;
    }
}