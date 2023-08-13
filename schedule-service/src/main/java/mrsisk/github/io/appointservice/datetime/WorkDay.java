package mrsisk.github.io.appointservice.datetime;

import java.util.TreeSet;

public record WorkDay(String dayOfWeek, String dayOfMonth, String fullDate, TreeSet<WorkPeriod> workPeriods) {
}
