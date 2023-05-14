package com.kolosya.zavodsimulator.factory;

public abstract class CarPart {
    protected long id;

    public CarPart(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
    }
}
