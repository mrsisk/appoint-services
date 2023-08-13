package mrsisk.github.io.appointservice.datetime;

import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TimeFactory {

    public TreeSet<WorkPeriod> generate(LocalDate date) {

        DayOfWeek dayOfWeek = DayOfWeek.from(date);
        Map<String, Session> sessions = generateSessions(dayOfWeek, date);

        return sessions.entrySet()
                .stream()
                .flatMap(v -> Stream.of(generateWorkPeriods(v.getValue())))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private TreeSet<WorkPeriod> generateWorkPeriods(Session session) {
        long remainder = session.sessionDuration() % session.interval().toMinutes();
        WorkPeriod seed = new WorkPeriod(session.start(), session.start().plus(session.interval()), session.type());
        TreeSet<WorkPeriod> workPeriods = Stream
                .iterate(seed, workPeriod -> workPeriod.getEnd().isBefore(session.end()) || workPeriod.getEnd().equals(session.end()),
                        p -> new WorkPeriod(p.getEnd(), p.getEnd().plus(session.interval()), session.type()))
                .collect(Collectors.toCollection(TreeSet::new));

        if (remainder > 0){
            WorkPeriod lastPeriod = workPeriods.pollLast();
            if (lastPeriod != null){
                WorkPeriod copy = new WorkPeriod(lastPeriod.getStart(), lastPeriod.getEnd().plusMinutes(remainder), lastPeriod.getSession());
                workPeriods.add(copy);
            }
        }
        return workPeriods;
    }



    private Map<String, Session> generateSessions(DayOfWeek dayOfWeek, LocalDate date) {

        Map<String, Session> sessions = new HashMap<>();

        Session morning = switch (dayOfWeek) {

            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY ->
                    new Session(ZonedDateTime.of(date, LocalTime.of(8, 0), ZoneId.of("Africa/Johannesburg")), ZonedDateTime.of(date, LocalTime.of(13, 0), ZoneId.of("Africa/Johannesburg")), Duration.ofMinutes(60), SessionType.MORNING);

            case FRIDAY -> new Session(ZonedDateTime.of(date, LocalTime.of(9, 0), ZoneId.of("Africa/Johannesburg")), ZonedDateTime.of(date, LocalTime.of(13, 0), ZoneId.of("Africa/Johannesburg")), Duration.ofMinutes(60), SessionType.MORNING);

            default -> throw new IllegalArgumentException("Day of week out of range" + dayOfWeek);
        };
        sessions.put("Morning", morning);
        Session afternoon = switch (dayOfWeek) {

            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY ->
                    new Session(ZonedDateTime.of(date, LocalTime.of(14, 0), ZoneId.of("Africa/Johannesburg")), ZonedDateTime.of(date, LocalTime.of(17, 0), ZoneId.of("Africa/Johannesburg")), Duration.ofMinutes(60), SessionType.AFTERNOON);

            case FRIDAY -> new Session(ZonedDateTime.of(date, LocalTime.of(14, 0), ZoneId.of("Africa/Johannesburg")), ZonedDateTime.of(date, LocalTime.of(16, 0), ZoneId.of("Africa/Johannesburg")), Duration.ofMinutes(60), SessionType.AFTERNOON);

            default -> throw new IllegalArgumentException("Day of week out of range" + dayOfWeek);
        };
        sessions.put("Afternoon", afternoon);

        return sessions;

    }


}
