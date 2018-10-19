package webservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventsStatisticsController {

    private final AtomicLong counter = new AtomicLong();
    private TimeEventsStatistics eventsStatisticsPerMinute = new TimeEventsStatistics(1,  60)
            , eventsStatisticsPerHour = new TimeEventsStatistics(60, 60),
            eventsStatisticsPerDay = new TimeEventsStatistics(24 * 60, 24);

    @RequestMapping("/eventsStatistics")
    public EventsStatistics eventsStatistics(
            @RequestParam(value="statistic", defaultValue="null")
                                                         String interval) {
        long time = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss SS");
        eventsStatisticsPerMinute.addEvent(time);
        eventsStatisticsPerHour.addEvent(time);
        eventsStatisticsPerDay.addEvent(time);
        System.out.println("New event added " + dateFormat.format(new Date()));
        switch (interval){
            case "minute":
                return new EventsStatistics(counter.incrementAndGet(),
                        String.format("За последнюю минуту вашу новость посмотрели %d раз"
                                , eventsStatisticsPerMinute.getCountOfEvents()));
            case "hour":
                return new EventsStatistics(counter.incrementAndGet(),
                        String.format("За последний час вашу новость посмотрели %d раз"
                                , eventsStatisticsPerHour.getCountOfEvents()));
            case "day":
                return new EventsStatistics(counter.incrementAndGet(),
                        String.format("За последний день вашу новость посмотрели %d раз"
                                , eventsStatisticsPerDay.getCountOfEvents()));
        }
        return new EventsStatistics(counter.incrementAndGet()
                , "Если вы хотите узнать стаистику за определенный промежуток времени, введите " +
                " в запросе с параметром statistic один из трех интервалов : " +
                "1) hour, 2) minute, 3) day");
    }

}
