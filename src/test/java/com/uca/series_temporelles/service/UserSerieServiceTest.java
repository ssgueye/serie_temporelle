package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.repository.AppUserRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSerieServiceTest {

    @Mock
    UserSerieRepository userSerieRepository;
    @Mock
    SerieRepository serieRepository;
    @Mock
    AppUserRepository appUserRepository;

    @InjectMocks
    UserSerieService userSerieService;
    @InjectMocks
    SerieService serieService;
    @InjectMocks
    AppUserService appUserService;



    private static UserSerieEntity newUserSerieEntity(Permission permission,
                                                      Boolean isOwner,
                                                      AppUserEntity userEntity,
                                                      SerieEntity serieEntity){
        UserSerieEntity entity = new UserSerieEntity();
        entity.permission = permission;
        entity.isOwner = isOwner;
        entity.serie = serieEntity;
        entity.appUser = userEntity;

        return entity;
    }


}
