package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
