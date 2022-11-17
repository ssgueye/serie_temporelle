package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;

public class AppUser {
    @JsonProperty
    public String pseudo;
    @JsonIgnore
    public List<UserSerie> userSeries = new LinkedList<>();

    public AppUser(@JsonProperty("pseudo") String pseudo) {
        Assert.hasText(pseudo, "Pseudo can not be null/empty/blank");
        Assert.doesNotContain(pseudo, " ", "Pseudo can not contain white spaces");
        this.pseudo = pseudo.toLowerCase();
    }

}
