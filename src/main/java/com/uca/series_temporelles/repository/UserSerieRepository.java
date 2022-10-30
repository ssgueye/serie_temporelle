package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.UserSerieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSerieRepository extends JpaRepository<UserSerieEntity, Long> {
}
