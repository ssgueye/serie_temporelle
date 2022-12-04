package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.UserSerie;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @InjectMocks
    UserSerieService userSerieService;
    @Mock
    AppUserService appUserService;

    private final static LocalDateTime date = LocalDateTime.of(2022, 12, 2, 19, 59);


    @Nested
    class TestGetUserSeries{

        @Test
        void mustReturnAllExistingUserie(){
            var user = newUserEntity("ssgueye");
            var serie1 = newSerieEntity(1L, "title1", "desc");
            var serie2= newSerieEntity(2L, "title2", null);
            var serie3 = newSerieEntity(3L, "title3", null);
            var userSeries = new UserSerieEntity[]{
                    newUserSerieEntity(1L, Permission.WRITE_READ, true,user, serie1),
                    newUserSerieEntity(2L, Permission.READONLY, false,user, serie2),
                    newUserSerieEntity(3L, Permission.WRITE_READ, true,user, serie3),

            };
            when(userSerieRepository.getUserSerieEntitiesByAppUserPseudo("ssgueye"))
                    .thenReturn(Arrays.asList(userSeries));

            var result = userSerieService.getAllUserSeries("ssgueye");

            assertThat(result).isNotNull();
            assertThat(result)
                    .extracting(s -> Tuple.tuple(s.id_user_serie, s.permission, s.isOwner, s.appUser, s.serie))
                    .containsExactly(
                            Tuple.tuple(1L, Permission.WRITE_READ, true,user, serie1),
                            Tuple.tuple(2L, Permission.READONLY, false,user, serie2),
                            Tuple.tuple(3L, Permission.WRITE_READ, true,user, serie3));

            verify(userSerieRepository).getUserSerieEntitiesByAppUserPseudo(eq("ssgueye"));
        }

        @Test
        void mustReturnAnExistingUserSerie(){
            var user = newUserEntity("ssgueye");
            var serie = newSerieEntity(1L, "title1", "desc");
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId("ssgueye", 1L))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true,user, serie));

            var result = userSerieService.getOne("ssgueye", 1L);
            assertThat(result).isNotNull();
            assertThat(result.id_user_serie).isEqualTo(1L);
            assertThat(result.permission).isEqualTo(Permission.WRITE_READ);
            assertTrue(result.isOwner);
            assertThat(result.appUser).isEqualTo(user);
            assertThat(result.serie).isEqualTo(serie);

            verify(userSerieRepository).getUserSerieEntityByUserPseudoAndSerieId(eq("ssgueye"), eq(1L));
        }
    }

    @Nested
    class TestForSharingUserSeries{

        @Test
        void mustReturnExceptionWhenSharing(){
            assertThrows(IllegalArgumentException.class,
                    ()-> userSerieService.shareSerie(null,
                            "ssgueye",
                            "dapieu",
                            Permission.READONLY));

            assertThrows(IllegalArgumentException.class,
                    ()-> userSerieService.shareSerie(1L,
                            null,
                            "dapieu",
                            Permission.READONLY));

            assertThrows(IllegalArgumentException.class,
                    ()-> userSerieService.shareSerie(1L,
                            "ssgueye",
                            null,
                            Permission.READONLY));

            assertThrows(IllegalArgumentException.class,
                    ()-> userSerieService.shareSerie(1L,
                            "ssgueye",
                            "dapieu",
                            null));
            assertThrows(IllegalArgumentException.class,
                    ()-> userSerieService.shareSerie(1L,
                            "",
                            "dapieu",
                            Permission.WRITE_READ));
            assertThrows(IllegalArgumentException.class,
                    ()-> userSerieService.shareSerie(1L,
                            " ",
                            "dapieu",
                            Permission.READONLY));
        }

        @Test
        void canNotShareSerieIfNotTheOwner(){
            var user = newUserEntity("ssgueye");
            var serie = newSerieEntity(1L, "title1", "desc");
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId("ssgueye", 1L))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, false,user, serie));

            assertThrows(NoAccessDataException.class,
                    ()-> userSerieService.shareSerie(
                    1L,
                    "ssgueye",
                    "dapieu",
                    Permission.READONLY));

        }
        @Test
        void canShareSerieToAnotherUser(){
            var userOwner = newUserEntity("ssgueye");
            var userReceiver = newUserEntity("dapieu");
            var serie = newSerieEntity(1L, "title1", "desc");
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId("ssgueye", 1L))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true,userOwner, serie));

            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId("dapieu", 1L))
                    .thenReturn(null);
            when(serieRepository.findById(any())).thenReturn(Optional.of(serie));
            when(appUserService.getOne("dapieu")).thenReturn(userReceiver);
            when(userSerieRepository.save(any()))
                    .thenReturn(newUserSerieEntity(2L, Permission.READONLY, false,userReceiver, serie));

            var result = userSerieService
                    .shareSerie(1L,
                            "ssgueye",
                            "dapieu",
                            Permission.READONLY);

            assertThat(result).isNotNull();
            assertThat(result.id_user_serie).isEqualTo(2L);
            assertThat(result.permission).isEqualTo(Permission.READONLY);
            assertFalse(result.isOwner);
            assertThat(result.appUser).isEqualTo(userReceiver);
            assertThat(result.serie).isEqualTo(serie);

            var captor = ArgumentCaptor.forClass(UserSerieEntity.class);
            verify(userSerieRepository).save(captor.capture());
            var userSerie = captor.getValue();
            assertThat(userSerie).isNotNull();
            assertThat(userSerie.permission).isEqualTo(Permission.READONLY);
            assertFalse(userSerie.isOwner);
            assertThat(userSerie.appUser).isEqualTo(userReceiver);
            assertThat(userSerie.serie).isEqualTo(serie);
        }

        @Test
        void UpdateSharedPermissionWhenSharingSerieTwiceTheSameUser(){
            var userOwner = newUserEntity("ssgueye");
            var userReceiver = newUserEntity("dapieu");
            var serie = newSerieEntity(1L, "title1", "desc");
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId("ssgueye", 1L))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true,userOwner, serie));

            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId("dapieu", 1L))
                    .thenReturn(newUserSerieEntity(2L, Permission.READONLY, false,userReceiver, serie));
            when(userSerieRepository.save(any()))
                    .thenReturn(newUserSerieEntity(2L, Permission.READONLY, false,userReceiver, serie));

            var result = userSerieService
                    .shareSerie(1L,
                            "ssgueye",
                            "dapieu",
                            Permission.WRITE_READ); //We are changing the permission for the receiver

            assertThat(result).isNotNull();
            assertThat(result.id_user_serie).isEqualTo(2L);
            assertThat(result.permission).isEqualTo(Permission.READONLY);
            assertFalse(result.isOwner);
            assertThat(result.appUser).isEqualTo(userReceiver);
            assertThat(result.serie).isEqualTo(serie);

            var captor = ArgumentCaptor.forClass(UserSerieEntity.class);
            verify(userSerieRepository).save(captor.capture());
            var userSerie = captor.getValue();
            assertThat(userSerie).isNotNull();
            assertThat(userSerie.id_user_serie).isEqualTo(2L);
            assertThat(userSerie.permission).isEqualTo(Permission.WRITE_READ);
            assertFalse(userSerie.isOwner);
            assertThat(userSerie.appUser).isEqualTo(userReceiver);
            assertThat(userSerie.serie).isEqualTo(serie);

        }

        @Test
        void mustReturnOwnerUserSerieWhenHeIsSharingSerieToHimSelf(){
            var userOwner = newUserEntity("ssgueye");
            var userReceiver = newUserEntity("ssgueye");
            var serie = newSerieEntity(1L, "title1", "desc");
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId("ssgueye", 1L))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true,userOwner, serie));

            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId("ssgueye", 1L))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true,userReceiver, serie));

            var result = userSerieService
                    .shareSerie(1L,
                            "ssgueye",
                            "ssgueye",
                            Permission.READONLY); //We are changing the permission for the receiver

            assertThat(result).isNotNull();
            assertThat(result.id_user_serie).isEqualTo(1L);
            assertThat(result.permission).isEqualTo(Permission.WRITE_READ);
            assertTrue(result.isOwner);
            assertThat(result.appUser).isEqualTo(userReceiver);
            assertThat(result.serie).isEqualTo(serie);

        }
    }

    @Test
    void mustConvertUserSerieToEntity(){
        UserSerie userSerie = new UserSerie(Permission.READONLY,true,
                new AppUser("ssgueye"), new Serie("title", "desc"));
        assertThat(userSerieService.toUserSerieEntity(userSerie)).isNotNull();
    }

    @Test
    void mustConvertEntityToUserSerie(){
        assertThat(userSerieService.toSerieEntity(new Serie("title1", "desc"))).isNotNull();
    }

    private static UserSerieEntity newUserSerieEntity(Long id,
                                                      Permission permission,
                                                      Boolean isOwner,
                                                      AppUserEntity userEntity,
                                                      SerieEntity serieEntity){
        UserSerieEntity entity = new UserSerieEntity();
        entity.id_user_serie = id;
        entity.permission = permission;
        entity.isOwner = isOwner;
        entity.serie = serieEntity;
        entity.appUser = userEntity;

        return entity;
    }

    private SerieEntity newSerieEntity(Long id, String title, String desc){
        SerieEntity serie = new SerieEntity();
        serie.id_serie = id;
        serie.title = title;
        serie.description = desc;
        serie.lastUpdatedDate = date;

        return serie;
    }

    private static AppUserEntity newUserEntity(String pseudo){
        AppUserEntity entity = new AppUserEntity();
        entity.pseudo = pseudo;

        return entity;
    }


}
