package com.kolosya.zavodsimulator.factory;

import com.kolosya.zavodsimulator.Shutdownable;
import com.kolosya.zavodsimulator.threadpool.ThreadPool;

public class SupplierThreadPool<T extends CarPart> extends Thread implements Shutdownable {
    private final ThreadPool threadPool;
    private Storage<T> storage;
    private final CarPartCreator<T> creator;
    private long id = 0;
    private boolean isRunning = true;
    private final int tasksCount;

    public SupplierThreadPool(Storage<T> storage, CarPartCreator<T> creator, int threadsCount, long sleepTime) {
        this.storage = storage;
        this.creator = creator;
        this.threadPool = new ThreadPool(threadsCount, sleepTime);
        this.tasksCount = threadsCount * 2;
    }

    public void shutdown() {
        isRunning = false;
        threadPool.shutdown();
        interrupt();
    }

    public void setStorage(Storage<T> storage) {
        this.storage = storage;
    }

    public void setSleepTime(long sleepTime) {
        threadPool.setSleepTime(sleepTime);
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            if (threadPool.tasksCount() >= tasksCount) {
                threadPool.waitForTasks();
            }

            this.threadPool.execute(() -> {
                var product = this.creator.create(id++);
                try {
                    storage.put(product);
                } catch (InterruptedException ignored) {
                    return;
                }
                Debug.getInstance().log(String.format("%s %d was delivered to storage by supplier %d",
                        product.getClass().getSimpleName(), product.getID(), threadPool.getThreadID(Thread.currentThread())));
            });
        }
    }
}
