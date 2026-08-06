package com.factory.pool;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
    private final PooledThread[] threads;
    private final Queue<Runnable> taskQueue;
    private volatile boolean running = true;

    public ThreadPool(int threadCount) {
        taskQueue = new LinkedList<>();
        threads = new PooledThread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new PooledThread("Worker-" + i, taskQueue);
            threads[i].start();
        }
    }

    public void submit(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    public int getQueueSize() {
        synchronized (taskQueue) {
            return taskQueue.size();
        }
    }

    public void shutdown() {
        running = false;
        for (PooledThread thread : threads) {
            thread.interrupt();
        }
    }

    private class PooledThread extends Thread {
        private final Queue<Runnable> taskQueue;

        public PooledThread(String name, Queue<Runnable> taskQueue) {
            super(name);
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            while (running) {
                Runnable task = null;

                synchronized (taskQueue) {
                    while (taskQueue.isEmpty() && running) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                    if (!running) {
                        return;
                    }

                    task = taskQueue.poll();
                }

                if (task != null) {
                    try {
                        task.run();
                    } catch (Exception e) {
                        System.err.println(getName() + " error: " + e.getMessage());
                    }
                }
            }
        }
    }
}