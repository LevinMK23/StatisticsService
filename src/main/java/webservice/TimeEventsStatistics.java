package webservice;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class TimeEventsStatistics extends Thread {

    private ConcurrentLinkedDeque<AtomicLong> queue;
    private AtomicLong countOfEvents;
    private int countOfQuantum;
    private long quantumTime, windowInMinutes;
    private boolean running;

    public TimeEventsStatistics(long windowInMinutes, int countOfQuantum) {
        this.countOfQuantum = countOfQuantum;
        this.windowInMinutes = windowInMinutes;
        countOfEvents = new AtomicLong(0);
        quantumTime = 60 * 1000 * windowInMinutes / countOfQuantum;
        queue = new ConcurrentLinkedDeque<>();
        queue.add(new AtomicLong(0));
        running = true;
    }

    public long getTimeWindowSize(){
        return windowInMinutes;
    }

    public void setTimeWindowSize(long windowSize, int quantums){
        windowInMinutes = windowSize;
        countOfQuantum = quantums;
        quantumTime = 60 * 1000 * windowInMinutes / countOfQuantum;
    }

    @Override
    public String toString() {
        return String.valueOf(getCountOfEvents());
    }

    @Override
    public void run(){
        while (running){
            try {
                Thread.sleep(quantumTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(queue.peekLast().get() + " events in last " + this.quantumTime / 1000 + " seconds");
            queue.addLast(new AtomicLong(0));
            while (queue.size() > countOfQuantum) queue.pollFirst();
        }
    }

    @Override
    public void interrupt() {
        running = false;
    }

    public void addEvent(long time){
        queue.peekLast().incrementAndGet();
    }

    public long getCountOfEvents(){
        countOfEvents.set(0);
        for (AtomicLong a : queue) {
            countOfEvents.addAndGet(a.get());
        }
        return countOfEvents.get();
    }

}