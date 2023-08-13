package mrsisk.github.io.appointservice.datetime;

import java.time.ZonedDateTime;

public class WorkPeriod implements Comparable<WorkPeriod> {

    private ZonedDateTime start;
    private ZonedDateTime end;

    private SessionType session;

    public WorkPeriod(ZonedDateTime start, ZonedDateTime end, SessionType session) {
        this.start = start;
        this.end = end;
        this.session = session;
    }


    @Override
    public int compareTo(WorkPeriod o) {
        return start.compareTo(o.start);
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public SessionType getSession() {
        return session;
    }

    public void setSession(SessionType session) {
        this.session = session;
    }


}
