package com.uca.series_temporelles.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SERIE")
public class Serie {
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "DESCRIPTION", nullable = true)
    private String description;
    @Column(name = "LAST_UPDATEd_DATE")
    private LocalDateTime lastUpdatedDate;

    public Serie( String title, String description) {
        this.title = title;
        this.description = description;
    }
    public Serie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
