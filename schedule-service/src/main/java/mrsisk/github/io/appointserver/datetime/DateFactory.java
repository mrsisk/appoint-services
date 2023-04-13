package mrsisk.github.io.appointserver.datetime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.stream.Stream;

@Component
public class DateFactory {
    private final LocalDate now = LocalDate.now(ZoneId.of("Africa/Johannesburg"));
    private  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private TimeFactory timeFactory;


    private final List<DayOfWeek> dayOfWeeks = Stream.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).toList();

    private final List<DayOfWeek> offDays = Stream.of(DayOfWeek.TUESDAY).toList();
    private final TemporalAdjuster weekendAdjuster = d -> (DayOfWeek.from(d) != DayOfWeek.FRIDAY) ?
            d.plus(1, ChronoUnit.DAYS) : d.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

    private final TemporalAdjuster nowAdjuster = d -> (dayOfWeeks.contains(DayOfWeek.from(d)) ?
            d.with(TemporalAdjusters.next(DayOfWeek.MONDAY)) : d);

    public List<WorkDay> workingDays(int num){

        LocalDate seed = now.with(nowAdjuster);

        return Stream.iterate(seed, d -> d.with(weekendAdjuster))
                .limit(num)
                .map(date -> {

                    TreeSet<WorkPeriod> workPeriods = timeFactory.generate(date);
                    return workDayConvertor(date, workPeriods);
                })
                .toList();
    }

    private WorkDay workDayConvertor(LocalDate date, TreeSet<WorkPeriod> workPeriods){
        final String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        final String fullDate = date.format(formatter);
        final int day = date.getDayOfMonth();
        final String formattedDay = String.format("%02d", day);
        return new WorkDay(dayOfWeek, formattedDay, fullDate, workPeriods);
    }
}

