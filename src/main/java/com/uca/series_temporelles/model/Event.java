package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Event {

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime event_date;
    @JsonProperty
    public Double value;
    @JsonProperty
    public String comment;
    @JsonIgnore
    public LocalDateTime lastUpdatedDate;
    @JsonIgnore
    public Serie serie;
    @JsonIgnore
    List<Tag> tags = new LinkedList<>();

    public Event(@JsonProperty("event_date") LocalDateTime date,
                 @JsonProperty("value") Double value,
                 @JsonProperty("commment") String comment,
                 Serie serie) {
        Assert.notNull(value, "An event should have a value");
        Assert.notNull(serie, "An event should be linked to a Serie");
        this.event_date = date;
        this.value = value;
        this.comment = comment;
        this.lastUpdatedDate = LocalDateTime.now();
        this.serie = serie;
    }
}
