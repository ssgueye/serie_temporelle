package com.uca.series_temporelles.model;

import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Table(name = "APP_USER")
public class AppUser {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PSEUDO", unique = true)
    private String pseudo;

    public AppUser(){}

    public AppUser(String pseudo) {
        setPseudo(pseudo);
    }

    public Long getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        Assert.hasText(pseudo, "Pseudo can not be null/empty/blank");
        Assert.doesNotContain(pseudo, " ", "Pseudo can not contain white spaces");
        this.pseudo = pseudo.toLowerCase();
    }


}
