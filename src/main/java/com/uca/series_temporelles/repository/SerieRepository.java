package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.SerieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SerieRepository extends JpaRepository<SerieEntity, Long> {

    @Query("SELECT s FROM UserSerieEntity us, SerieEntity s WHERE us.serie.id_serie = s.id_serie AND us.appUser.pseudo = ?1")
    Iterable<SerieEntity> getAllSeriesByPseudo(String pseudo);

    @Query("SELECT s FROM UserSerieEntity us, SerieEntity s WHERE us.serie.id_serie = s.id_serie " +
            "AND us.appUser.pseudo = ?1 AND us.isOwner = true")
    Iterable<SerieEntity> getAllOwnSeriesByPseudo(String pseudo);

    @Query("SELECT s FROM UserSerieEntity us, SerieEntity s WHERE us.serie.id_serie = s.id_serie " +
            "AND us.appUser.pseudo = ?1 AND us.isOwner = false")
    Iterable<SerieEntity> getAllSharedSeriesByPseudo(String pseudo);
}
