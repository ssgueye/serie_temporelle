package com.uca.series_temporelles.model;

import com.uca.series_temporelles.enumerations.Privilege;

public class UserSerie {
    public Privilege privilege;
    public boolean isOwner;
    public AppUser appUser;
    private Serie serie;

    public UserSerie(Privilege privilege, boolean isOwner, AppUser appUser, Serie serie) {
        this.privilege = privilege;
        this.isOwner = isOwner;
        this.appUser = appUser;
        this.serie = serie;
    }


}
