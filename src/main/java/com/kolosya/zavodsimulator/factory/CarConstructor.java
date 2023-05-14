package com.kolosya.zavodsimulator.factory;

public class CarConstructor {
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Body> bodyStorage;
    private long id = 0;

    public CarConstructor(Storage<Accessory> accessoryStorage, Storage<Motor> motorStorage, Storage<Body> bodyStorage) {
        this.accessoryStorage = accessoryStorage;
        this.motorStorage = motorStorage;
        this.bodyStorage = bodyStorage;
    }

    public Car construct() {
        var motor = motorStorage.get();
        var accessory = accessoryStorage.get();
        var body = bodyStorage.get();
        long _id;
        synchronized (this) {
            _id = id++;
        }
        return new Car(motor, accessory, body, _id);
    }
}
