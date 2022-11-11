package com.uca.series_temporelles.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_SERIE")
public class UserSerie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRIVILEGE")
    private String privilege;

    @Column(name = "IS_OWNER")
    private boolean isOwner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUser")
    private AppUser user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idSerie")
    private Serie serie;


    public UserSerie(String privilege, boolean isOwner, AppUser user, Serie serie) {
        this.privilege = privilege;
        this.isOwner = isOwner;
        this.user = user;
        this.serie = serie;
    }

    public UserSerie() {
    }

    public Long getId() {
        return id;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }
}
