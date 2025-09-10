package com.metodosanaliticos.app;

import java.util.PriorityQueue;

public class Scheduler {
    public PriorityQueue<Event> eventQueue;

    public Scheduler() {
        eventQueue = new PriorityQueue<>();
    }

    public void scheduleEvent(Event event) {
        eventQueue.offer(event);
    }

    public Event getNextEvent() {
        return eventQueue.poll();
    }

    public boolean hasEvents() {
        return !eventQueue.isEmpty();
    }
}
