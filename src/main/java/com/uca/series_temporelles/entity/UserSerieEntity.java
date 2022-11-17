package com.uca.series_temporelles.entity;

import com.uca.series_temporelles.enumerations.Permission;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "USER_SERIE")
public class UserSerieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Long id_user_serie;

    @Column(name = "PRIVILEGE")
    @Enumerated(EnumType.STRING)
    public Permission permission;

    @Column(name = "OWNER")
    public boolean isOwner;

    @ManyToOne()
    @JoinColumn(name = "USER_PSEUDO")
    public AppUserEntity appUser;

    @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ID_SERIE")
    public SerieEntity serie;

}
