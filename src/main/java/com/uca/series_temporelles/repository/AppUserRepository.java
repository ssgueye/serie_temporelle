package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AppUserRepository extends JpaRepository<AppUser,Long> {

}
