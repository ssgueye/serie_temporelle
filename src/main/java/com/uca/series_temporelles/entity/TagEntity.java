package com.uca.series_temporelles.entity;

import javax.persistence.*;

@Entity
@Table(name = "TAG")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "LABEL")
    public String label;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_EVENT")
    public EventEntity event;

}
