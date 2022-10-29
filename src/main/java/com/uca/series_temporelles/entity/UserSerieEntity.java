package com.uca.series_temporelles.entity;

import com.uca.series_temporelles.enumerations.Privilege;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.model.Serie;

import javax.persistence.*;

@Entity
@Table(name = "USER_SERIE")
public class UserSerieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Long id;

    @Column(name = "PRIVILEGE")
    @Enumerated(EnumType.STRING)
    public Privilege privilege;

    @Column(name = "OWNER")
    public boolean isOwner;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_USER")
    public AppUserEntity appUser;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_SERIE")
    public SerieEntity serie;

}
