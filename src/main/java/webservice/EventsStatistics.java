package webservice;

public class EventsStatistics {

    private long id;
    private final String content;

    public EventsStatistics(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
