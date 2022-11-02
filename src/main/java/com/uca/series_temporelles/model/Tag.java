package com.uca.series_temporelles.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TAG")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long idTag;
    @Column(name = "LABEL")
    private String label;
    @Column(name = "EVENT",nullable = false)
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "EVENTS_TAGS",
                joinColumns = @JoinColumn(name = "idTag"),
                inverseJoinColumns = @JoinColumn(name = "idEvent"))

    private List<Event> event = new ArrayList<Event>();

    public Tag(String label, List<Event> event) {
        this.label = label;
        this.event = event;
    }

    public Tag() {
    }

    public Long getId() {
        return idTag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Event> getEvent() {
        return event;
    }

    public void setEvent(List<Event> event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "label='" + label + '\'' +
                ", event=" + event +
                '}';
    }
}
