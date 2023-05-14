package com.kolosya.zavodsimulator.factory;

import com.kolosya.zavodsimulator.Shutdownable;
import com.kolosya.zavodsimulator.threadpool.ThreadPool;

public class DealersThread extends Thread implements Shutdownable {
    private final ThreadPool dealersPool;
    private final int tasksCount;
    private final Storage<Car> carStorage;
    private boolean isRunning = true;
    private boolean logSale = true;

    public DealersThread(Storage<Car> carStorage, ThreadPool dealersPool, boolean logSale) {
        this.carStorage = carStorage;
        this.dealersPool = dealersPool;
        this.tasksCount = dealersPool.getThreadsCount() * 2;
        this.logSale = logSale;
    }

    public void shutdown() {
        isRunning = false;
        interrupt();
        dealersPool.shutdown();
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            if (tasksCount <= dealersPool.tasksCount()) {
                dealersPool.waitForTasks();
            }

            dealersPool.execute(() -> {
                Car car = null;
                synchronized (this) {
                    try {
                        car = carStorage.get();
                    } catch (InterruptedException ignored) {
                        return;
                    }
                }
                if (!logSale) {
                    return;
                }
                Debug.getInstance().log(String.format("Car %d <Motor %d; Accessory %d; Body %d> was sold by dealer %d.",
                        car.getID(), car.getMotor().getID(), car.getAccessory().getID(), car.getBody().getID(), dealersPool.getThreadID(Thread.currentThread())));
            });
        }
    }
}
