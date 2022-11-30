package com.uca.series_temporelles.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "TAG")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "LABEL")
    public String label;

    @ManyToOne()
    @JoinColumn(name = "ID_EVENT", nullable = false)
    @JsonIgnore
    public EventEntity event;

}
