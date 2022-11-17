package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uca.series_temporelles.enumerations.Permission;
import org.springframework.util.Assert;


public class UserSerie {

    @JsonProperty
    public Permission permission;
    public Boolean isOwner;
    @JsonIgnore
    public AppUser appUser;

    public Serie serie;


    public UserSerie(@JsonProperty("permission") Permission permission, Boolean isOwner, AppUser appUser, Serie serie) {

        Assert.notNull(permission, "privilege must not be null");
        this.permission = isOwner ? Permission.WRITE_READ : permission; //Just for safety
        this.isOwner = isOwner;
        this.appUser = appUser;
        this.serie = serie;
    }


}
