package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie,Long> {
}
