package com.metodosanaliticos.app;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private double globalTime = 0.0;
    private final int MAX_RANDONS = 100000;

    private Scheduler scheduler;
    private List<Queue> queues;
    private NumberGenerator numberGenerator;
    private int randomsUsed = 0;

    public Simulator(List<Queue> queues, NumberGenerator numberGenerator) {
        this.queues = queues;
        this.numberGenerator = numberGenerator;
        this.scheduler = new Scheduler();
    }

    public Simulator(Queue[] queues, NumberGenerator numberGenerator) {
        this.queues = new ArrayList<>();
        for (Queue queue : queues) {
            this.queues.add(queue);
        }
        this.numberGenerator = numberGenerator;
        this.scheduler = new Scheduler();
    }

    public void run() {
        for (int i = 0; i < queues.size(); i++) {
            Queue queue = queues.get(i);
            if (queue.arrivalMinTime > 0) {  
                double firstArrivalTime = calculateTime(queue.arrivalMinTime, queue.arrivalMaxTime);
                Event firstEvent = new Event(i, firstArrivalTime, EventType.CHEGADA, -1, i);
                scheduler.scheduleEvent(firstEvent);
            }
        }
        
        double lastTime = 0.0;

        while (randomsUsed < MAX_RANDONS && scheduler.hasEvents()) {
            Event event = scheduler.getNextEvent();
            
            globalTime = event.time;
            double deltaTime = globalTime - lastTime;

            for (Queue q : queues) {
                if(q.clientsInServer <= q.capacity) {
                    q.accTimesInQueue[q.clientsInServer] += deltaTime;
                }
            }

            switch (event.type) {
                case CHEGADA:
                    processChegada(event);
                    break;
                case SAIDA:
                    processSaida(event);
                    break;
                case PASSAGEM:
                    processPassagem(event);
                    break;
            }
            lastTime = globalTime;
        }
        
        printResults();
    }

    private double calculateTime(double min, double max) {
        this.randomsUsed++;
        return min + (max - min) * numberGenerator.nextRandom();
    }

    /**
     * Determina o destino do roteamento probabilístico baseado nas probabilidades definidas
     * @param queue Fila de origem
     * @return ID da fila de destino (-1 para exterior)
     */
    private int determineRoutingDestination(Queue queue) {
        if (queue.routingDestinations == null || queue.routingProbabilities == null) {
            return -1;
        }

        this.randomsUsed++;
        double randomValue = numberGenerator.nextRandom();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < queue.routingProbabilities.length; i++) {
            cumulativeProbability += queue.routingProbabilities[i];
            if (randomValue <= cumulativeProbability) {
                return queue.routingDestinations[i];
            }
        }

        return queue.routingDestinations[queue.routingDestinations.length - 1];
    }

    /**
     * Processa evento de chegada de cliente
     * @param event Evento de chegada
     */
    private void processChegada(Event event) {
        Queue queue = queues.get(event.queueId);
        
        if (queue.arrivalMinTime > 0 && randomsUsed < MAX_RANDONS) {
            double timeToNextChegada = this.globalTime + calculateTime(queue.arrivalMinTime, queue.arrivalMaxTime);
            scheduler.scheduleEvent(new Event(queue.id, timeToNextChegada, EventType.CHEGADA, -1, queue.id));
        }
        
        if (!queue.isFull()) {
            queue.clientsInServer++;
            if (queue.hasAvailableServer() && randomsUsed < MAX_RANDONS) {
                double serviceTime = calculateTime(queue.serviceMinTime, queue.serviceMaxTime);
                scheduler.scheduleEvent(new Event(queue.id, this.globalTime + serviceTime, EventType.SAIDA, queue.id, -1));
            }
        } else {
            queue.losses++;
        }
    }

    /**
     * Processa evento de saída de cliente
     * @param event Evento de saída
     */
    private void processSaida(Event event) {
        Queue queue = queues.get(event.queueId);
        queue.clientsInServer--;

        if (randomsUsed < MAX_RANDONS) {
            int destinationId = determineRoutingDestination(queue);
            
            if (destinationId != -1) {
                Queue destinationQueue = queues.get(destinationId);
                if (!destinationQueue.isFull()) {
                    destinationQueue.clientsInServer++;
                    if (destinationQueue.hasAvailableServer() && randomsUsed < MAX_RANDONS) {
                        double serviceTime = calculateTime(destinationQueue.serviceMinTime, destinationQueue.serviceMaxTime);
                        scheduler.scheduleEvent(new Event(destinationId, this.globalTime + serviceTime, EventType.SAIDA, destinationId, -1));
                    }
                } else {
                    destinationQueue.losses++;
                }
            }
        }

        if (queue.clientsInServer >= queue.servers && randomsUsed < MAX_RANDONS) {
            double serviceTime = calculateTime(queue.serviceMinTime, queue.serviceMaxTime);
            scheduler.scheduleEvent(new Event(queue.id, this.globalTime + serviceTime, EventType.SAIDA, queue.id, -1));
        }
    }

    /**
     * Processa evento de passagem (não utilizado)
     * @param event Evento de passagem
     */
    private void processPassagem(Event event) {
    }

    public void printResults() {
        System.out.println("==============================================================");
        System.out.println("               RELATÓRIO DA SIMULAÇÃO");
        System.out.println("==============================================================");
        System.out.printf("Tempo Global de Simulação: %.4f\n", this.globalTime);
        System.out.printf("Total de Números Aleatórios Usados: %d\n", this.randomsUsed);
        System.out.println("--------------------------------------------------------------");

        for (Queue q : queues) {
            System.out.printf("\n>>> FILA %d (G/G/%d/%d)\n", q.id + 1, q.servers, q.capacity);
            System.out.printf("Total de Perdas: %d\n\n", q.losses);
            System.out.println("Estado | Tempo Acumulado    | Probabilidade (%)");
            System.out.println("----------------------------------------------------");
            for (int i = 0; i <= q.capacity; i++) {
                double probability = (q.accTimesInQueue[i] / this.globalTime) * 100.0;
                System.out.printf("   %d   | %-18.4f | %.4f%%\n", i, q.accTimesInQueue[i], probability);
            }
        }
        System.out.println("\n==============================================================");
    }
}

