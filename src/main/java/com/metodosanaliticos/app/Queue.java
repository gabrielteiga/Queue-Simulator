package com.metodosanaliticos.app;

public class Queue {
    public int id;
    public int capacity;
    public int servers;
    public int clientsInServer = 0;
    public double[] accTimesInQueue;
    public int losses = 0;
    
    public double arrivalMinTime;
    public double arrivalMaxTime;
    public double serviceMinTime;
    public double serviceMaxTime;
    public int[] routingDestinations;
    public double[] routingProbabilities;

    public Queue(int id, int capacity, int servers, double arrivalMinTime, double arrivalMaxTime, 
                 double serviceMinTime, double serviceMaxTime) {
        this.id = id;
        this.capacity = capacity;
        this.servers = servers;
        this.arrivalMinTime = arrivalMinTime;
        this.arrivalMaxTime = arrivalMaxTime;
        this.serviceMinTime = serviceMinTime;
        this.serviceMaxTime = serviceMaxTime;
        this.accTimesInQueue = new double[capacity + 1];
    }

    public Queue(int id, int capacity, int servers) {
        this(id, capacity, servers, 1.0, 4.0, 2.0, 3.0);
    }

    /**
     * Configura o roteamento probabilístico da fila
     * @param destinations IDs das filas de destino (-1 para exterior)
     * @param probabilities Probabilidades correspondentes (devem somar 1.0)
     */
    public void setRouting(int[] destinations, double[] probabilities) {
        this.routingDestinations = destinations.clone();
        this.routingProbabilities = probabilities.clone();
        
        double sum = 0.0;
        for (double prob : probabilities) {
            sum += prob;
        }
        if (Math.abs(sum - 1.0) > 1e-6) {
            throw new IllegalArgumentException("As probabilidades de roteamento devem somar 1.0, mas somam: " + sum);
        }
    }

    /**
     * Verifica se a fila está cheia
     * @return true se a fila está cheia
     */
    public boolean isFull() {
        return this.clientsInServer >= this.capacity;
    }

    /**
     * Verifica se a fila está vazia
     * @return true se a fila está vazia
     */
    public boolean isEmpty() {
        return this.clientsInServer == 0;
    }

    /**
     * Verifica se há servidor disponível
     * @return true se há servidor disponível
     */
    public boolean hasAvailableServer() {
        return this.clientsInServer <= this.servers;
    }
}

