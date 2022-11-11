package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.model.UserSerie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSerieRepository extends JpaRepository<UserSerie,Long> {
}
