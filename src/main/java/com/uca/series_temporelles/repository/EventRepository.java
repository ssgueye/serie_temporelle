package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.EventEntity;
import com.uca.series_temporelles.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity,Long> {

    Iterable<EventEntity> getEventEntitiesBySerieId(Long serie_id);
}
