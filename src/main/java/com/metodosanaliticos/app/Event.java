package com.metodosanaliticos.app;

public class Event implements Comparable<Event> {
    public double time;
    public EventType type; 
    public int queueId;
    public int sourceQueueId;
    public int destinationQueueId;

    public Event(int queueId, double time, EventType type) {
        this.time = time;
        this.type = type;
        this.queueId = queueId;
        this.sourceQueueId = -1;
        this.destinationQueueId = -1;
    }

    public Event(int queueId, double time, EventType type, int sourceQueueId, int destinationQueueId) {
        this.time = time;
        this.type = type;
        this.queueId = queueId;
        this.sourceQueueId = sourceQueueId;
        this.destinationQueueId = destinationQueueId;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}
