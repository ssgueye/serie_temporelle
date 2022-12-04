package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public class Event{


    public LocalDateTime event_date;
    public Double value;
    public String comment;
    @JsonIgnore
    public LocalDateTime lastUpdatedDate;
    @JsonIgnore
    public Serie serie;


    public Event(LocalDateTime event_date,
                 Double value,
                 String comment) {
        Assert.notNull(event_date, "event_date can not be null");
        Assert.notNull(value, "An event should have a value");
        this.value = value;
        this.comment = comment;
        this.event_date = event_date;
        this.lastUpdatedDate = LocalDateTime.now();
    }
}
