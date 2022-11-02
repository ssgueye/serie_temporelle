package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EVENT")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long idEvent;

    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;
    @Column(name = "EVENT_VALUE")
    private String eventValue;
    @Column(name = "LAST_UPDATE_DATE")
    private LocalDateTime lastUpdateDate;
    @Column(name = "TAG")
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "EVENTS_TAGS",
            joinColumns = @JoinColumn(name = "idEvent"),
            inverseJoinColumns = @JoinColumn(name = "idTag"))
    private List<Tag> tags = new ArrayList<Tag>();

    public Event(LocalDateTime eventDate, String eventValue, List<Tag> tags) {
        this.eventDate = eventDate;
        this.eventValue = eventValue;
        this.tags = tags;
    }
    public Event() {
    }

    public Long getId() {
        return idEvent;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}

