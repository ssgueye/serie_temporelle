package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uca.series_temporelles.enumerations.Privilege;
import org.springframework.util.Assert;


public class UserSerie {
    @JsonProperty
    public Privilege privilege;
    @JsonIgnore
    public Boolean isOwner;
    @JsonIgnore
    public AppUser appUser;
    @JsonIgnore
    public Serie serie;


    public UserSerie(@JsonProperty("privilege") Privilege privilege,Boolean isOwner, AppUser appUser, Serie serie) {
        Assert.notNull(appUser, "Should have a User linked to this class");
        Assert.notNull(serie, "Should have a Serie linked to this class");
        Assert.notNull(isOwner, "isOwner must not be null");
        Assert.notNull(privilege, "privilege must not be null");
        this.privilege = isOwner ? Privilege.WRITE_READ : privilege; //Just for safety
        this.isOwner = isOwner;
        this.appUser = appUser;
        this.serie = serie;
    }


}
