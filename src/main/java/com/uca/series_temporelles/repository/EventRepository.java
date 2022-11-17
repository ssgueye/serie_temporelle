package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity,Long> {

    @Query(value = "SELECT e FROM EventEntity e WHERE e.serie.id_serie = ?1")
    Iterable<EventEntity> getAllEventsBySerieId(Long serie_id);
}
