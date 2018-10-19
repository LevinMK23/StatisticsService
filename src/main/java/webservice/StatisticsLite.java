package webservice;

import java.util.concurrent.atomic.AtomicLong;

public class StatisticsLite {

    private AtomicLong eventsPerMinute, eventsPerHour, eventsPerDay;
    private long timeM;
    private long timeH;
    private long timeD;

    public StatisticsLite() {
        eventsPerDay = new AtomicLong(0);
        eventsPerMinute = new AtomicLong(0);
        eventsPerHour = new AtomicLong(0);
        long now = System.currentTimeMillis();
        timeD = now;
        timeH = now;
        timeM = now;
    }

    public void addEvent(long eventMoment){
        if(eventMoment - timeM >= 60 * 1000){
            eventsPerMinute.set(0);
            timeM = eventMoment;
        }
        if(eventMoment - timeH >= 60 * 60 * 1000){
            eventsPerHour.addAndGet(-eventsPerMinute.get());
            timeH = eventMoment;
        }
        if(eventMoment - timeD >= 24 * 60 * 60 * 1000){
            eventsPerDay.addAndGet(-eventsPerHour.get());
            timeD = eventMoment;
        }
        eventsPerMinute.incrementAndGet();
        eventsPerHour.incrementAndGet();
        eventsPerDay.incrementAndGet();
    }

    public long getEventsPerMinute() {
        return eventsPerMinute.get();
    }

    public long getEventsPerHour() {
        return eventsPerHour.get();
    }

    public long getEventsPerDay() {
        return eventsPerDay.get();
    }
}
