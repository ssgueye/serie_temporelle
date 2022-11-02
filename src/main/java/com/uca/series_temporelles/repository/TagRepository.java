package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
}
