package com.kolosya.zavodsimulator;

import com.kolosya.zavodsimulator.factory.ControllerFactoryContext;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class ZavodController {
    public static ControllerFactoryContext context;

    @FXML
    private Slider motorSlider;

    @FXML
    private Slider bodySlider;

    @FXML
    private Slider accessorySlider;

    @FXML
    private Slider workersSlider;

    @FXML
    private Slider dealersSlider;

    @FXML
    private void onMotorSleepTimeChanged() {
        context.getMotorSupplier().setSleepTime((long)(motorSlider.getValue() * 1000));
    }

    @FXML
    private void onBodySleepTimeChanged() {
        context.getBodySupplier().setSleepTime((long)(bodySlider.getValue() * 1000));
    }

    @FXML
    private void onAccessorySleepTimeChanged() {
        context.getAccessorySuppliersPool().setSleepTime((long)(accessorySlider.getValue() * 1000));
    }

    @FXML
    private void onWorkersSleepTimeChanged() {
        context.getWorkersPool().setSleepTime((long)(workersSlider.getValue() * 1000));
    }

    @FXML
    private void onDealersSleepTimeChanged() {
        context.getDealersPool().setSleepTime((long)(dealersSlider.getValue() * 1000));
    }
}