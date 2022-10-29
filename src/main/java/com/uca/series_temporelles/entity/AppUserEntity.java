package com.uca.series_temporelles.entity;

import javax.persistence.*;

@Entity
@Table(name = "APP_USER")
public class AppUserEntity {

    @Id
    @Column(name = "PSEUDO")
    public String pseudo;
}
