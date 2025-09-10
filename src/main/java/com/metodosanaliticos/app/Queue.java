package com.metodosanaliticos.app;

public class Queue {
    public int capacity;
    public int servers;
    public int clientsInQueue = 0;
    public double accTimesInQueue[];
    public int id;

    public Queue(int id, int capacity, int servers) {
        this.id = id;
        this.capacity = capacity;
        this.servers = servers;
        this.accTimesInQueue = new double[capacity + 1];
    }

    public boolean isFull() {
        return this.clientsInQueue >= this.capacity;
    }

    public boolean isEmpty() {
        return this.clientsInQueue == 0;
    }
}
