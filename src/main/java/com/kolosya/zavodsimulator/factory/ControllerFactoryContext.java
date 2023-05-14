package com.kolosya.zavodsimulator.factory;

import com.kolosya.zavodsimulator.factory.Body;
import com.kolosya.zavodsimulator.factory.Motor;
import com.kolosya.zavodsimulator.factory.SupplierThread;
import com.kolosya.zavodsimulator.threadpool.ThreadPool;

public interface ControllerFactoryContext {
    ThreadPool getWorkersPool();
    ThreadPool getDealersPool();

    SupplierThreadPool getAccessorySuppliersPool();

    SupplierThread<Motor> getMotorSupplier();
    SupplierThread<Body> getBodySupplier();
}
