package mrsisk.github.io.appointservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mrsisk.github.io.appointservice.datetime.SessionType;


import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userId;
    private LocalDate date;
    private ZonedDateTime start;
    private ZonedDateTime end;

    private SessionType sessionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Agent agent;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking(String userId, LocalDate date, ZonedDateTime start, ZonedDateTime end, SessionType sessionType, Agent agent, Status status) {
        this.userId = userId;
        this.date = date;
        this.start = start;
        this.end = end;
        this.sessionType = sessionType;
        this.agent = agent;
        this.status = status;
    }

    public Booking() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
