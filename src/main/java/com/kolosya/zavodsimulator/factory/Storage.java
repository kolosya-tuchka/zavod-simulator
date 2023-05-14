package com.kolosya.zavodsimulator.factory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Storage<T> {
    private int capacity;
    private final Queue<T> elements;
    private final WaitIn waitIn;
    private final WaitOut waitOut;

    private static class WaitIn{}
    private static class WaitOut{}

    public Storage(int capacity) {
        this.capacity = capacity;
        this.elements = new ConcurrentLinkedQueue<>();
        this.waitIn = new WaitIn();
        this.waitOut = new WaitOut();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getElementsCount() {
        return elements.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public void put(T element) throws InterruptedException {
        synchronized (waitIn) {
            if (elements.size() == capacity) {
                waitIn.wait();
            }
        }

        synchronized (elements) {
            elements.add(element);
        }
        synchronized (this) {
            notifyAll();
        }
        synchronized (waitOut) {
            waitOut.notify();
        }
    }

    public T get() throws InterruptedException {
        T element = null;

        synchronized (waitOut) {
            if (elements.isEmpty()) {
                waitOut.wait();
                element = elements.remove();
            }
        }

        if (element == null) {
            element = elements.remove();
        }
        synchronized (this) {
            notifyAll();
        }
        synchronized (waitIn) {
            waitIn.notify();
        }
        return element;
    }
}
