package com.kolosya.zavodsimulator;

import com.kolosya.zavodsimulator.factory.*;
import com.kolosya.zavodsimulator.threadpool.ThreadPool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ZavodApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ZavodApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        stage.setTitle("Factory");
        stage.setScene(scene);
        stage.setOnCloseRequest(ZavodApplication::shutdown);
        stage.show();
    }

    private static void shutdown(WindowEvent windowEvent) {
        if (windowEvent.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
            for (var shut : shutdownables) {
                shut.shutdown();
            }
        }
    }

    private static Shutdownable[] shutdownables;

    public static void main(String[] args) throws IOException {
        new Debug("factory.log", true);

        var config = new FactoryConfig(new File("src/main/java/com/kolosya/zavodsimulator/storageConfig.txt"));

        Storage<Motor> motorStorage = new Storage<>(Integer.parseInt(config.getProperty("StorageMotorSize")));
        Storage<Accessory> accessoryStorage = new Storage<>(Integer.parseInt(config.getProperty("StorageAccessorySize")));
        Storage<Body> bodyStorage = new Storage<>(Integer.parseInt(config.getProperty("StorageBodySize")));
        Storage<Car> carStorage = new Storage<>(Integer.parseInt(config.getProperty("StorageAutoSize")));
        ThreadPool workersPool = new ThreadPool(Integer.parseInt(config.getProperty("Workers")), 5000);
        ThreadPool dealersPool = new ThreadPool(Integer.parseInt(config.getProperty("Dealers")), 5000);

        SupplierThread<Body> bodySupplierThread = new SupplierThread<>(bodyStorage, new BodyCreator(), 5000);
        SupplierThread<Motor> motorSupplierThread = new SupplierThread<>(motorStorage, new MotorCreator(), 5000);
        SupplierThreadPool<Accessory> accessorySupplierThreadPool = new SupplierThreadPool<>(accessoryStorage, new AccessoryCreator(), Integer.parseInt(config.getProperty("AccessorySuppliers")), 5000);

        CarStorageController carStorageController = new CarStorageController(new CarConstructor(accessoryStorage, motorStorage, bodyStorage), carStorage, workersPool);

        DealersThread dealersThread = new DealersThread(carStorage, dealersPool, Boolean.parseBoolean(config.getProperty("LogSale")));

        bodySupplierThread.start();
        motorSupplierThread.start();
        accessorySupplierThreadPool.start();
        carStorageController.start();
        dealersThread.start();

        ZavodController.context = new ControllerFactoryContext() {
            @Override
            public ThreadPool getWorkersPool() {
                return workersPool;
            }

            @Override
            public ThreadPool getDealersPool() {
                return dealersPool;
            }

            @Override
            public SupplierThreadPool<Accessory> getAccessorySuppliersPool() {
                return accessorySupplierThreadPool;
            }

            @Override
            public SupplierThread<Motor> getMotorSupplier() {
                return motorSupplierThread;
            }

            @Override
            public SupplierThread<Body> getBodySupplier() {
                return bodySupplierThread;
            }
        };

        shutdownables = new Shutdownable[] {
            workersPool,
            dealersThread,
            bodySupplierThread,
            motorSupplierThread,
            accessorySupplierThreadPool,
            carStorageController,
            Debug.getInstance()
        };

        launch();
    }
}