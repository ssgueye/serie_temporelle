package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.repository.AppUserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    AppUserRepository appUserRepository;

    @InjectMocks
    AppUserService appUserService;


    @Test
    void mustReturnAllExistingUsers(){
        var userEntities = new AppUserEntity[] {
            newUserEntity("ssgueye"),
            newUserEntity("saliou"),
            newUserEntity("dapieu")
        };

        when(appUserRepository.findAll()).thenReturn(Arrays.asList(userEntities));

        var appUsers = appUserService.getAllUsers();

        assertThat(appUsers)
                .extracting(f -> Tuple.tuple(f.pseudo))
                .containsExactly(
                        Tuple.tuple("ssgueye"),
                        Tuple.tuple("saliou"),
                        Tuple.tuple("dapieu"));

        verify(appUserRepository).findAll();
    }

    @Test
    void canNotSaveNullUser(){
        assertThrows(IllegalArgumentException.class, ()-> appUserService.save(null));
    }

    @Test
    void canNotGetAppUserWithoutItsPseudo(){
        assertThrows(IllegalArgumentException.class, ()-> appUserService.getOne(null));
        assertThrows(IllegalArgumentException.class, ()-> appUserService.getOne(""));
        assertThrows(IllegalArgumentException.class, ()-> appUserService.getOne("  "));
    }

    @Test
    void canNotReturnNullUsersList(){
        var result = appUserService.getAllUsers();
        assertThat(result).isNotNull();
    }

    @Test
    void canNotReturnNullElementsInTheUsersList(){
        var result = appUserService.getAllUsers();
        assertThat(result).doesNotContainNull();
    }

    @Test
    void mustReturnExceptionForAnInExistingUserInRepository(){
        when(appUserRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> appUserService.getOne("saliou"));

        verify(appUserRepository).findById(eq("saliou"));
    }

    @Test
    void mustReturnExistingUserInRepository(){
        when(appUserRepository.findById(any())).thenReturn(Optional.of(newUserEntity("saliou")));

        var user = appUserService.getOne("saliou");
        assertThat(user.pseudo).isEqualTo("saliou");

        verify(appUserRepository).findById(eq("saliou"));
    }

    @Test
    void mustReturnSavingUser(){
        AppUserEntity appUser = newUserEntity("dapieu");

        when(appUserRepository.save(any())).thenReturn(appUser);

        var user = appUserService.save(new AppUser("saliou"));

        assertThat(user.pseudo).isEqualTo("dapieu");

        var captor = ArgumentCaptor.forClass(AppUserEntity.class);

        verify(appUserRepository).save(captor.capture());

        var arg = captor.getValue();
        assertThat(arg.pseudo).isEqualTo("saliou");
    }

    @Test
    void convertEntityToUser(){
        assertThat(appUserService.toUser(newUserEntity("dapieu")));
    }

    private static AppUserEntity newUserEntity(String pseudo){
        AppUserEntity entity = new AppUserEntity();
        entity.pseudo = pseudo;

        return entity;
    }
}
