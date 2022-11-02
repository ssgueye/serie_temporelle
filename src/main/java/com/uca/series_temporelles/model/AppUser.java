package com.uca.series_temporelles.model;

import javax.persistence.*;

@Entity
@Table(name = "APP_USER")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    private String pseudo;

    public AppUser(String pseudo) {
        this.pseudo = pseudo;
    }
    public AppUser() {
    }

    public Long getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
