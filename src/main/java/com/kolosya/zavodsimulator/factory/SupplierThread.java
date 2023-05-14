package com.kolosya.zavodsimulator.factory;

import com.kolosya.zavodsimulator.Shutdownable;

public class SupplierThread<T extends CarPart> extends Thread implements Shutdownable {
    private boolean isRunning = true;
    private final CarPartCreator<T> creator;
    private Storage<T> storage;
    private long sleepTime;
    private long id = 0;

    public SupplierThread(Storage<T> storage, CarPartCreator<T> creator, long sleepTime) {
        this.storage = storage;
        this.creator = creator;
        this.sleepTime = sleepTime;
    }

    public void setStorage(Storage<T> storage) {
        this.storage = storage;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void shutdown() {
        isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            var product = creator.create(id++);
            storage.put(product);
            Debug.getInstance().log(String.format("%s %d was delivered to storage by supplier",
                    product.getClass().getSimpleName(), product.getID()));

            if (!isRunning) {
                break;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
                //TODO: implement me
            }
        }
    }
}
