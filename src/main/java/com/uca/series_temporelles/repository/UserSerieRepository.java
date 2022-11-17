package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.UserSerieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserSerieRepository extends JpaRepository<UserSerieEntity, Long> {
    Iterable<UserSerieEntity> getUserSerieEntitiesByAppUserPseudo(String pseud_user);

    @Query(value = "SELECT us FROM UserSerieEntity us WHERE us.appUser.pseudo = ?1 AND us.serie.id_serie = ?2")
    UserSerieEntity getUserSerieEntityByUserPseudoAndSerieId(String pseudo_user, Long serie_id);

    @Query(value = "SELECT us FROM UserSerieEntity us WHERE us.serie.id_serie = ?1")
    Iterable<UserSerieEntity> getAllUserSeriesBySerieId(Long serie_id);
}
