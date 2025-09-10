package com.metodosanaliticos.app;

public class Event implements Comparable<Event> {
    public double time;
    public EventType type; 
    public int queueId;

    public Event(int queueId, double time, EventType type) {
        this.time = time;
        this.type = type;
        this.queueId = queueId;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}
