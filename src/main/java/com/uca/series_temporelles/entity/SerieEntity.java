package com.uca.series_temporelles.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SERIE")
public class SerieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "TITLE")
    public String title;

    @Column(name = "DESCRIPTION")
    public String description;

    @Column(name = "LAST_UPDATED_DATE")
    public LocalDateTime lastUpdatedDate;
}
