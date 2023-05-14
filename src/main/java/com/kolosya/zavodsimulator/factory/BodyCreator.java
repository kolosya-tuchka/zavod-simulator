package com.kolosya.zavodsimulator.factory;

public class BodyCreator extends CarPartCreator<Body> {
    @Override
    public Body create(long id) {
        return new Body(id);
    }
}
