package com.factory.ui;

import com.factory.FactoryManager;
import com.factory.config.Configuration;
import com.factory.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.concurrent.ThreadPoolExecutor;

public class FactoryUI extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(FactoryUI.class);
    
    private final FactoryManager factoryManager;
    
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
        this.factoryManager = new FactoryManager(config);
        
        logger.info("Initializing Factory UI");
        initUI();
        
        factoryManager.initFactory();
        factoryManager.startFactory();
        
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

    private void stopFactory() {
        logger.info("Stopping factory");
        running = false;
        updateTimer.stop();
        factoryManager.stopFactory();
        dispose();
    }

    private void updateStats() {
        bodyCountLabel.setText(String.format("%d/%d", 
            factoryManager.getBodyStorage().getCurrentSize(), 
            factoryManager.getBodyStorage().getCapacity()));
        engineCountLabel.setText(String.format("%d/%d", 
            factoryManager.getEngineStorage().getCurrentSize(), 
            factoryManager.getEngineStorage().getCapacity()));
        accessoryCountLabel.setText(String.format("%d/%d", 
            factoryManager.getAccessoryStorage().getCurrentSize(), 
            factoryManager.getAccessoryStorage().getCapacity()));
        carCountLabel.setText(String.format("%d/%d", 
            factoryManager.getCarStorage().getCurrentSize(), 
            factoryManager.getCarStorage().getCapacity()));
        
        totalBodyProducedLabel.setText(String.valueOf(factoryManager.getBodyStorage().getTotalProduced()));
        totalEngineProducedLabel.setText(String.valueOf(factoryManager.getEngineStorage().getTotalProduced()));
        totalAccessoryProducedLabel.setText(String.valueOf(factoryManager.getAccessoryStorage().getTotalProduced()));
        
        carsBuiltLabel.setText(String.valueOf(Worker.getCarsBuilt()));
        carsSoldLabel.setText(String.valueOf(factoryManager.getCarStorage().getTotalSold()));
        
        if (factoryManager.getWorkerPool() instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) factoryManager.getWorkerPool();
            queueSizeLabel.setText(String.valueOf(executor.getQueue().size()));
        }
    }

    private void updateBodySupplierSpeed(ChangeEvent e) {
        int delay = bodySupplierSpeedSlider.getValue();
        factoryManager.getBodySuppliers().forEach(s -> s.setDelay(delay));
    }

    private void updateEngineSupplierSpeed(ChangeEvent e) {
        int delay = engineSupplierSpeedSlider.getValue();
        factoryManager.getEngineSuppliers().forEach(s -> s.setDelay(delay));
    }

    private void updateAccessorySupplierSpeed(ChangeEvent e) {
        int delay = accessorySupplierSpeedSlider.getValue();
        factoryManager.getAccessorySuppliers().forEach(s -> s.setDelay(delay));
    }

    private void updateDealerSpeed(ChangeEvent e) {
        int delay = dealerSpeedSlider.getValue();
        factoryManager.getDealers().forEach(d -> d.setDelay(delay));
    }
}