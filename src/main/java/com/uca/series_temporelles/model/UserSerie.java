package com.uca.series_temporelles.model;

import com.uca.series_temporelles.enumerations.Privilege;

import javax.persistence.*;

@Entity
@Table(name = "USER_SERIE")
public class UserSerie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRIVILEGE")
    @Enumerated(EnumType.STRING)
    private Privilege privilege;

    @Column(name = "owner")
    private boolean isOwner;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USER_ID")
    AppUser appUser;

    public UserSerie() {
    }

    public UserSerie(Privilege privilege, boolean isOwner, AppUser appUser) {
        this.privilege = privilege;
        this.isOwner = isOwner;
        this.appUser = appUser;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public AppUser getAppUser() {
        return appUser;
    }

}
