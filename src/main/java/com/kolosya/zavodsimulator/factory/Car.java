package com.kolosya.zavodsimulator.factory;

public class Car {
    private final long id;
    private final Motor motor;
    private final Accessory accessory;
    private final Body body;

    public Car(Motor motor, Accessory accessory, Body body, long id) {
        this.id = id;
        this.motor = motor;
        this.accessory = accessory;
        this.body = body;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    public Body getBody() {
        return body;
    }

    public Motor getMotor() {
        return motor;
    }

    public long getID() {
        return id;
    }
}
