package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.entity.UserSerieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserSerieRepository extends JpaRepository<UserSerieEntity, Long> {
    Iterable<UserSerieEntity> getUserSerieEntitiesByAppUserPseudo(String pseud_user);
    UserSerieEntity getUserSerieEntitiesByAppUserPseudoAndSerieId(String pseudo_user, Long serie_id);
    Iterable<UserSerieEntity> getUserSerieEntitiesBySerieId(Long serie_id);
}
