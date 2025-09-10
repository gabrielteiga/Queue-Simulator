package com.metodosanaliticos.app;

public class Simulator {
    private double globalTime = 0.0;
    private final int MAX_RANDONS = 100000;

    private Scheduler scheduler;
    private Queue[] queues;
    private NumberGenerator numberGenerator;
    private int randomsUsed = 0;

    public Simulator(Queue[] queues, NumberGenerator numberGenerator) {
        this.queues = queues;
        this.numberGenerator = numberGenerator;
        this.scheduler = new Scheduler();
    }

    public void run() {
        Event firstEvent = new Event(0, 1.5, EventType.CHEGADA);
        scheduler.scheduleEvent(firstEvent);
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

    private void processChegada(Event event) {
        Queue queue = queues[event.queueId];
        
        double timeToNextChegada = this.globalTime + calculateTime(1, 4);
        scheduler.scheduleEvent(new Event(0, timeToNextChegada, EventType.CHEGADA));
        
        if (!queue.isFull()) {
            queue.clientsInServer++;
            if (queue.hasAvailableServer()) {
                double timePassagem = this.globalTime + calculateTime(3, 4);
                scheduler.scheduleEvent(new Event(1, timePassagem, EventType.PASSAGEM));
            }
        } else {
            queue.losses++;
        }
    }

    private void processSaida(Event event) {
        Queue queue = queues[event.queueId];
        queue.clientsInServer--;

        if (queue.clientsInServer >= queue.servers) {
            double timeToSaida = this.globalTime + calculateTime(2, 3);
            scheduler.scheduleEvent(new Event(queue.id, timeToSaida, EventType.SAIDA));
        }
    }

    private void processPassagem(Event event) {
        Queue queue1 = queues[0];
        Queue queue2 = queues[1];

        queue1.clientsInServer--;

        if (queue1.clientsInServer >= queue1.servers) {
            double timeToPassagem = this.globalTime + calculateTime(3, 4);
            scheduler.scheduleEvent(new Event(1, timeToPassagem, EventType.PASSAGEM));
        }

        if (!queue2.isFull()) {
            queue2.clientsInServer++;
            if (queue2.hasAvailableServer()) {
                double timeToSaida = this.globalTime + calculateTime(2, 3);
                scheduler.scheduleEvent(new Event(1, timeToSaida, EventType.SAIDA));
            }
        } else {
            queue2.losses++;
        }
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

