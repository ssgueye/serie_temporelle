package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.TagEntity;
import com.uca.series_temporelles.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity,Long> {

    @Query("SELECT tag FROM TagEntity tag WHERE tag.label = ?1 AND tag.event.id_event = ?2")
    Optional<TagEntity> getTagByLabelAndEventId(String label, Long id_event);
}
