package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.TagEntity;
import com.uca.series_temporelles.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity,Long> {
}
