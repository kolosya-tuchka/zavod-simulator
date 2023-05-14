package com.kolosya.zavodsimulator.factory;

public class AccessoryCreator extends CarPartCreator<Accessory> {
    @Override
    public Accessory create(long id) {
        return new Accessory(id);
    }
}
