package com.metodosanaliticos.app;

public class Queue {
    public int id;
    public int capacity;
    public int servers;
    public int clientsInServer = 0;
    public double[] accTimesInQueue;
    public int losses = 0;

    public Queue(int id, int capacity, int servers) {
        this.id = id;
        this.capacity = capacity;
        this.servers = servers;
        this.accTimesInQueue = new double[capacity + 1];
    }

    public boolean isFull() {
        return this.clientsInServer >= this.capacity;
    }

    public boolean isEmpty() {
        return this.clientsInServer == 0;
    }

    public boolean hasAvailableServer() {
        return this.clientsInServer <= this.servers;
    }
}

