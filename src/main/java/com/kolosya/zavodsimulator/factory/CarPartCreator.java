package com.kolosya.zavodsimulator.factory;

public abstract class CarPartCreator<T extends CarPart> {
    public abstract T create(long id);
}
