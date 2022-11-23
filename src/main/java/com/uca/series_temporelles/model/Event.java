package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public class Event{


    public LocalDateTime event_date;
    @JsonProperty
    public Double value;
    @JsonProperty
    public String comment;
    @JsonIgnore
    public LocalDateTime lastUpdatedDate;
    @JsonIgnore
    public Serie serie;


    public Event(LocalDateTime event_date,
                 @JsonProperty("value") Double value,
                 @JsonProperty("comment") String comment) {
        Assert.notNull(value, "An event should have a value");
        this.value = value;
        this.comment = comment;
        this.event_date = event_date;
        this.lastUpdatedDate = LocalDateTime.now();
    }
}
