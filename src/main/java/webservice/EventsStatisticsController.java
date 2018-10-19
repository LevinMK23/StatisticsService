package webservice;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventsStatisticsController {

    private final AtomicLong counter = new AtomicLong();
    private StatisticsLite statistics = new StatisticsLite();

    @RequestMapping("/eventsStatistics")
    public EventsStatistics eventsStatistics(@RequestParam(value="statistic", defaultValue="null") String interval) {

        statistics.addEvent(System.currentTimeMillis());

        switch (interval){
            case "minute":
                return new EventsStatistics(counter.incrementAndGet(),
                        String.format("За последнюю минуту вашу новость посмотрели %d раз", statistics.getEventsPerMinute()));
            case "hour":
                return new EventsStatistics(counter.incrementAndGet(),
                        String.format("За последний час вашу новость посмотрели %d раз", statistics.getEventsPerHour()));
            case "day":
                return new EventsStatistics(counter.incrementAndGet(),
                        String.format("За последний день вашу новость посмотрели %d раз", statistics.getEventsPerDay()));
        }
        return new EventsStatistics(counter.incrementAndGet(), "Если вы хотите узнать стаистику за определенный промежуток времени, введите " +
                " в запросе с параметром statistic один из трех интервалов : " +
                "1) hour, 2) minute, 3) day");
    }

}
