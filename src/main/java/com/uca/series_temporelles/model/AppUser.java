package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

public class AppUser {
    @JsonProperty
    public String pseudo;

    public AppUser(@JsonProperty("pseudo") String pseudo) {
        Assert.hasText(pseudo, "Pseudo can not be null/empty/blank");
        Assert.doesNotContain(pseudo, " ", "Pseudo can not contain white spaces");
        this.pseudo = pseudo.toLowerCase();
    }

}
