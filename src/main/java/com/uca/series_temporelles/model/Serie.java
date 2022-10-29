package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;
import java.time.LocalDateTime;

public class Serie {
    @JsonProperty
    public String title;
    @JsonProperty
    public String description;
    @JsonIgnore
    public LocalDateTime lastUpdatedDate;

    public Serie(@JsonProperty("title") String title, @JsonProperty("description") String description) {
        Assert.isTrue(title.length() <= 50, "Title can not exceed 50 characters");
        Assert.hasText(title, "Title can not be null/empty/blank");
        this.title = title;
        this.description = description;
        this.lastUpdatedDate = LocalDateTime.now();
    }

}
