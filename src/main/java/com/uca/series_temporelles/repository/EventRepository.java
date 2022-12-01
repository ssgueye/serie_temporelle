package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<EventEntity,Long> {

    @Query(value = "SELECT e FROM EventEntity e WHERE e.serie.id_serie = ?1")
    Iterable<EventEntity> getAllEventsBySerieId(Long serie_id);

    @Query("SELECT e FROM EventEntity e , TagEntity t, UserSerieEntity us " +
            "WHERE e.id_event = t.event.id_event AND us.serie.id_serie = e.serie.id_serie " +
            "AND t.label = ?1 AND us.appUser.pseudo = ?2")
    Iterable<EventEntity> FilterEventsByTag(String label, String pseudo);

    @Query("SELECT e FROM EventEntity e , TagEntity t, UserSerieEntity us " +
            "WHERE e.id_event = t.event.id_event AND us.serie.id_serie = e.serie.id_serie " +
            "AND t.label = ?1 AND us.appUser.pseudo = ?2 " +
            "AND t.event.event_date >= ?3 AND t.event.event_date <= ?4")
    Iterable<EventEntity> getTagFrequencyByEventDateRange(String label, String pseudo, LocalDateTime start_date, LocalDateTime end_date);
}
