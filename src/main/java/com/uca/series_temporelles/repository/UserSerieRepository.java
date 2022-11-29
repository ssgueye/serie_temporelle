package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.UserSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSerieRepository extends JpaRepository<UserSerie,Long> {

    @Query(value = "select privilege from UserSerie us where us.appUser.idUser = ?1 and us.serie.idSerie = ?2", nativeQuery = true)
    String getPrivilege(AppUser user, Serie serie);

    @Query(value = "select * from UserSerie us where us.isOwner = false and us.appUser.idUser = ?1 and us.serie.idSerie = ?2", nativeQuery = true)
    Iterable<UserSerie> getAllSharedItems(AppUser user, Serie serie);
}
