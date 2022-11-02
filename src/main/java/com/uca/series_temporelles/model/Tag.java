package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

public class Tag {

    @JsonProperty
    public String label;

    @JsonIgnore
    public Event event;

    public Tag(@JsonProperty("label") String label, Event event) {
        Assert.hasText(label, "Label must not be null/empty/blank");
        Assert.notNull(event, "A tag must have an event");
        this.label = label;
        this.event = event;
    }

    //Si une personne crée le même nom de taga, on peut conserver juste l'ancien au lieu de
    //faire une duplication ( A voir)
}
