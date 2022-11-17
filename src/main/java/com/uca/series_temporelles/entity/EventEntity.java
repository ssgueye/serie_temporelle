package com.uca.series_temporelles.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "EVENT")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Long id_event;

    @Column(name = "EVENT_DATE")
    public LocalDateTime date;

    @Column(name = "EVENT_VALUE")
    public Double value;

    @Column(name = "COMMENT")
    public String comment;

    @Column(name = "LAST_UPDATED_DATE")
    public LocalDateTime lastUpdatedDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_SERIE")
    public SerieEntity serie;

}
