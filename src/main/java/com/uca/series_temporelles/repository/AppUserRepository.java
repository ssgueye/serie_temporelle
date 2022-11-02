package com.uca.series_temporelles.repository;

import com.uca.series_temporelles.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
}
