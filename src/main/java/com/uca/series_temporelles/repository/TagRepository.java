package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity,Long> {

    @Query("SELECT t FROM TagEntity t WHERE t.event.id_event = ?1")
    Iterable<TagEntity> getTagByEventId(Long serie_Id);

    @Query("SELECT t FROM TagEntity t WHERE t.event.serie.id_serie = ?1")
    Iterable<TagEntity> getTagsBySerieId(Long serieId);
}
