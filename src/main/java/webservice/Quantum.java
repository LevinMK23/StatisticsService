package webservice;

import java.util.concurrent.atomic.AtomicLong;

public class Quantum {

    private AtomicLong counter;
    private long time;

    public Quantum(long time) {
        this.time = time;
        counter = new AtomicLong(0);
    }

    public AtomicLong getCounter() {
        return counter;
    }

    public long getTime() {
        return time;
    }
}
