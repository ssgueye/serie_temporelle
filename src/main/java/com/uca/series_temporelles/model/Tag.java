package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

public class Tag {

    @JsonProperty
    public String label;

    @JsonIgnore
    public Event event;

    public Tag(@JsonProperty("label") String label) {
        Assert.hasText(label, "Label must not be null/empty/blank");
        Assert.isTrue(label.length() <= 20, "Title can not exceed 20 characters");
        label = label.replaceAll(" ", "_").toUpperCase();
        this.label = label;
    }

}
