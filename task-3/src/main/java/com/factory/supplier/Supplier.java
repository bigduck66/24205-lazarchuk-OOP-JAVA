package com.factory.supplier;
import com.factory.model.Component;
import com.factory.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Supplier<T extends Component> extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Supplier.class);
    private final Storage<T> storage;
    private final ComponentFactory<T> factory;
    private volatile boolean running = true;
    private volatile int delay = 1000;

    @FunctionalInterface
    public interface ComponentFactory<T extends Component> {
        T create();
    }

    public Supplier(String name, Storage<T> storage, ComponentFactory<T> factory) {
        super(name);
        this.storage = storage;
        this.factory = factory;
        logger.info("Created supplier: {}", name);
    }

    public void setDelay(int delay) {
        this.delay = delay;
        logger.debug("Supplier {} delay set to {}ms", getName(), delay);
    }

    public void stopSupplier() {
        running = false;
        logger.info("Stopping supplier: {}", getName());
        interrupt();
    }

    @Override
    public void run() {
        logger.info("Supplier {} started", getName());
        while (running) {
            try {
                Thread.sleep(delay);
                T component = factory.create();
                logger.debug("Supplier {} produced {}", getName(), component);
                storage.put(component);
            } catch (InterruptedException e) {
                logger.info("Supplier {} interrupted", getName());
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Supplier {} stopped", getName());
    }
}