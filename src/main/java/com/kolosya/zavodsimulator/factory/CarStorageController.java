package com.kolosya.zavodsimulator.factory;

import com.kolosya.zavodsimulator.Shutdownable;
import com.kolosya.zavodsimulator.threadpool.ThreadPool;

public class CarStorageController extends Thread implements Shutdownable {
    private final CarConstructor constructor;
    private final Storage<Car> carStorage;
    private final ThreadPool workersPool;
    private final int tasksCount;
    private boolean isRunning = true;

    public CarStorageController(CarConstructor carConstructor, Storage<Car> carStorage, ThreadPool workersPool) {
        this.constructor = carConstructor;
        this.carStorage = carStorage;
        this.workersPool = workersPool;
        this.tasksCount = workersPool.getThreadsCount() * 2;
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            if (workersPool.tasksCount() >= tasksCount) {
                workersPool.waitForTasks();
            }

            workersPool.execute(() -> {
                Car car = null;
                try {
                    car = constructor.construct();
                } catch (InterruptedException ignored) {
                    return;
                }

                Debug.getInstance().log(String.format("Car %d was created by worker %d",
                        car.getID(), workersPool.getThreadID(Thread.currentThread())));
                try {
                    carStorage.put(car);
                } catch (InterruptedException ignored) {
                    return;
                }
                Debug.getInstance().log(String.format("Car %d was delivered to storage", car.getID()));
            });
        }
    }

    @Override
    public void shutdown() {
        isRunning = false;
        workersPool.shutdown();
        interrupt();
    }
}
