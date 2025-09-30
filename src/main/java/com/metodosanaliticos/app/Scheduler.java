package com.metodosanaliticos.app;

import java.util.PriorityQueue;

public class Scheduler {
    public PriorityQueue<Event> eventQueue;

    public Scheduler() {
        eventQueue = new PriorityQueue<>();
    }

    /**
     * Agenda um evento na fila de eventos
     * @param event Evento a ser agendado
     */
    public void scheduleEvent(Event event) {
        eventQueue.offer(event);
    }

    /**
     * Retorna o próximo evento a ser processado
     * @return Próximo evento ou null se não há eventos
     */
    public Event getNextEvent() {
        return eventQueue.poll();
    }

    /**
     * Verifica se há eventos na fila
     * @return true se há eventos pendentes
     */
    public boolean hasEvents() {
        return !eventQueue.isEmpty();
    }
}
