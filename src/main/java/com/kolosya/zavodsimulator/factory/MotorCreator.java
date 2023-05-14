package com.kolosya.zavodsimulator.factory;

public class MotorCreator extends CarPartCreator<Motor> {
    @Override
    public Motor create(long id) {
        return new Motor(id);
    }
}
