package com.factory.ui;

import com.factory.config.Configuration;
import com.factory.controller.FactoryController;
import com.factory.dealer.Dealer;
import com.factory.model.*;
import com.factory.storage.CarStorage;
import com.factory.storage.Storage;
import com.factory.supplier.Supplier;
import com.factory.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class FactoryUI extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(FactoryUI.class);
    
    private final Configuration config;
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final CarStorage carStorage;
    
    private final List<Supplier<Body>> bodySuppliers = new ArrayList<>();
    private final List<Supplier<Engine>> engineSuppliers = new ArrayList<>();
    private final List<Supplier<Accessory>> accessorySuppliers = new ArrayList<>();
    private final List<Dealer> dealers = new ArrayList<>();
    private ExecutorService workerPool;
    private FactoryController controller;
    
    private final JLabel bodyCountLabel = new JLabel("0/0");
    private final JLabel engineCountLabel = new JLabel("0/0");
    private final JLabel accessoryCountLabel = new JLabel("0/0");
    private final JLabel carCountLabel = new JLabel("0/0");
    private final JLabel carsBuiltLabel = new JLabel("0");
    private final JLabel carsSoldLabel = new JLabel("0");
    private final JLabel queueSizeLabel = new JLabel("0");
    private final JLabel totalBodyProducedLabel = new JLabel("0");
    private final JLabel totalEngineProducedLabel = new JLabel("0");
    private final JLabel totalAccessoryProducedLabel = new JLabel("0");
    
    private final JSlider bodySupplierSpeedSlider = new JSlider(100, 3000, 1000);
    private final JSlider engineSupplierSpeedSlider = new JSlider(100, 3000, 1000);
    private final JSlider accessorySupplierSpeedSlider = new JSlider(100, 3000, 1000);
    private final JSlider dealerSpeedSlider = new JSlider(100, 5000, 2000);
    
    private final Timer updateTimer;
    private volatile boolean running = true;

    public FactoryUI(Configuration config) {
        this.config = config;
        
        this.bodyStorage = new Storage<>(config.getStorageBodySize());
        this.engineStorage = new Storage<>(config.getStorageMotorSize());
        this.accessoryStorage = new Storage<>(config.getStorageAccessorySize());
        this.carStorage = new CarStorage(config.getStorageAutoSize());
        
        logger.info("Initializing Factory UI");
        initUI();
        initFactory();
        startFactory();
        
        updateTimer = new Timer(100, e -> updateStats());
        updateTimer.start();
    }

    private void initUI() {
        setTitle("Car Factory Emulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.NORTH);
        
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.CENTER);
        
        JButton stopButton = new JButton("Stop Factory");
        stopButton.addActionListener(e -> stopFactory());
        add(stopButton, BorderLayout.SOUTH);
        
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 5, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        panel.add(new JLabel("Body Storage:"));
        panel.add(bodyCountLabel);
        panel.add(new JLabel("Engine Storage:"));
        panel.add(engineCountLabel);
        panel.add(new JLabel("Accessory Storage:"));
        panel.add(accessoryCountLabel);
        panel.add(new JLabel("Car Storage:"));
        panel.add(carCountLabel);
        panel.add(new JLabel("Task Queue:"));
        panel.add(queueSizeLabel);
        
        panel.add(new JLabel("Bodies Produced:"));
        panel.add(totalBodyProducedLabel);
        panel.add(new JLabel("Engines Produced:"));
        panel.add(totalEngineProducedLabel);
        panel.add(new JLabel("Accessories Produced:"));
        panel.add(totalAccessoryProducedLabel);
        panel.add(new JLabel("Cars Built:"));
        panel.add(carsBuiltLabel);
        panel.add(new JLabel("Cars Sold:"));
        panel.add(carsSoldLabel);
        
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Speed Controls"));
        
        panel.add(new JLabel("Body Supplier Speed (ms):"));
        bodySupplierSpeedSlider.setPaintTicks(true);
        bodySupplierSpeedSlider.setPaintLabels(true);
        bodySupplierSpeedSlider.setMajorTickSpacing(500);
        bodySupplierSpeedSlider.addChangeListener(this::updateBodySupplierSpeed);
        panel.add(bodySupplierSpeedSlider);
        
        panel.add(new JLabel("Engine Supplier Speed (ms):"));
        engineSupplierSpeedSlider.setPaintTicks(true);
        engineSupplierSpeedSlider.setPaintLabels(true);
        engineSupplierSpeedSlider.setMajorTickSpacing(500);
        engineSupplierSpeedSlider.addChangeListener(this::updateEngineSupplierSpeed);
        panel.add(engineSupplierSpeedSlider);
        
        panel.add(new JLabel("Accessory Supplier Speed (ms):"));
        accessorySupplierSpeedSlider.setPaintTicks(true);
        accessorySupplierSpeedSlider.setPaintLabels(true);
        accessorySupplierSpeedSlider.setMajorTickSpacing(500);
        accessorySupplierSpeedSlider.addChangeListener(this::updateAccessorySupplierSpeed);
        panel.add(accessorySupplierSpeedSlider);
        
        panel.add(new JLabel("Dealer Speed (ms):"));
        dealerSpeedSlider.setPaintTicks(true);
        dealerSpeedSlider.setPaintLabels(true);
        dealerSpeedSlider.setMajorTickSpacing(1000);
        dealerSpeedSlider.addChangeListener(this::updateDealerSpeed);
        panel.add(dealerSpeedSlider);
        
        return panel;
    }

    private void initFactory() {
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
        
        workerPool = Executors.newFixedThreadPool(config.getWorkers());
        
        Runnable assemblyTask = () -> {
            Worker worker = new Worker(0, bodyStorage, engineStorage, accessoryStorage, carStorage);
            worker.run();
        };
        
        controller = new FactoryController(carStorage, workerPool, assemblyTask);
        
        for (int i = 0; i < config.getDealers(); i++) {
            Dealer dealer = new Dealer(i + 1, carStorage, config.isLogSale());
            dealer.setDelay(config.getDealerDelay());
            dealers.add(dealer);
        }
    }

    private void startFactory() {
        logger.info("Starting factory");
        bodySuppliers.forEach(Supplier::start);
        engineSuppliers.forEach(Supplier::start);
        accessorySuppliers.forEach(Supplier::start);
        controller.start();
        dealers.forEach(Dealer::start);
    }

    private void stopFactory() {
        logger.info("Stopping factory");
        running = false;
        updateTimer.stop();
        
        bodySuppliers.forEach(Supplier::stopSupplier);
        engineSuppliers.forEach(Supplier::stopSupplier);
        accessorySuppliers.forEach(Supplier::stopSupplier);
        controller.stopController();
        dealers.forEach(Dealer::stopDealer);
        workerPool.shutdown();
        
        dispose();
    }

    private void updateStats() {
        bodyCountLabel.setText(String.format("%d/%d", 
            bodyStorage.getCurrentSize(), bodyStorage.getCapacity()));
        engineCountLabel.setText(String.format("%d/%d", 
            engineStorage.getCurrentSize(), engineStorage.getCapacity()));
        accessoryCountLabel.setText(String.format("%d/%d", 
            accessoryStorage.getCurrentSize(), accessoryStorage.getCapacity()));
        carCountLabel.setText(String.format("%d/%d", 
            carStorage.getCurrentSize(), carStorage.getCapacity()));
        
        totalBodyProducedLabel.setText(String.valueOf(bodyStorage.getTotalProduced()));
        totalEngineProducedLabel.setText(String.valueOf(engineStorage.getTotalProduced()));
        totalAccessoryProducedLabel.setText(String.valueOf(accessoryStorage.getTotalProduced()));
        
        carsBuiltLabel.setText(String.valueOf(Worker.getCarsBuilt()));
        carsSoldLabel.setText(String.valueOf(carStorage.getTotalSold()));
        
        if (workerPool instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) workerPool;
            queueSizeLabel.setText(String.valueOf(executor.getQueue().size()));
        }
    }

    private void updateBodySupplierSpeed(ChangeEvent e) {
        int delay = bodySupplierSpeedSlider.getValue();
        bodySuppliers.forEach(s -> s.setDelay(delay));
    }

    private void updateEngineSupplierSpeed(ChangeEvent e) {
        int delay = engineSupplierSpeedSlider.getValue();
        engineSuppliers.forEach(s -> s.setDelay(delay));
    }

    private void updateAccessorySupplierSpeed(ChangeEvent e) {
        int delay = accessorySupplierSpeedSlider.getValue();
        accessorySuppliers.forEach(s -> s.setDelay(delay));
    }

    private void updateDealerSpeed(ChangeEvent e) {
        int delay = dealerSpeedSlider.getValue();
        dealers.forEach(d -> d.setDelay(delay));
    }
}