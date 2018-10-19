package webservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class TimeEventsStatistics {

    private ConcurrentLinkedDeque<Quantum> queue;
    private AtomicLong countOfEvents;
    private int countOfQuantum;
    private long quantumTime, windowInMinutes;
    public TimeEventsStatistics(long windowInMinutes, int countOfQuantum) {
        this.countOfQuantum = countOfQuantum;
        this.windowInMinutes = windowInMinutes;
        countOfEvents = new AtomicLong(0);
        quantumTime = 60 * 1000 * windowInMinutes / countOfQuantum;
        queue = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < countOfQuantum; i++) {
            queue.add(new Quantum(System.currentTimeMillis()));
        }
    }

    public long getTimeWindowSize(){
        return windowInMinutes;
    }

    public void addEvent(long time){
        long timeDifference = (time - queue.peekLast().getTime()) % (countOfQuantum * quantumTime);
        for (int i = 0; i < timeDifference / quantumTime; i++) {
            queue.addLast(new Quantum(queue.getLast().getTime() + quantumTime));
        }
        queue.peekLast().getCounter().incrementAndGet();
        while(queue.size() > countOfQuantum) queue.pollFirst();
    }


    public synchronized long getCountOfEvents(){
        countOfEvents.set(0);
        int i = 0;
        for (Quantum a : queue) {
            countOfEvents.addAndGet(a.getCounter().get());
        }
        return countOfEvents.get();
    }

}