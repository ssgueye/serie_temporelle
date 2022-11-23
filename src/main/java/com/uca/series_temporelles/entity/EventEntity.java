package com.uca.series_temporelles.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

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
    public LocalDateTime event_date;

    @Column(name = "EVENT_VALUE")
    public Double value;

    @Column(name = "COMMENT")
    public String comment;

    @Column(name = "LAST_UPDATED_DATE")
    public LocalDateTime lastUpdatedDate;

    @ManyToOne()
    @JoinColumn(name = "ID_SERIE")
    @JsonIgnore
    public SerieEntity serie;

}
