package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.service.AppUserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserControllerTest {

    @Mock
    AppUserService appUserService;

    @InjectMocks
    AppUserController appUserController;

    @Test
    void mustReturnAllExistingUsers(){
        var userEntities = new AppUserEntity[] {
                newUserEntity("ssgueye"),
                newUserEntity("saliou"),
                newUserEntity("dapieu")
        };

        when(appUserService.getAllUsers()).thenReturn(Arrays.asList(userEntities));

        var appUsers = appUserController.getAllUsers();

        assertThat(appUsers.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(appUsers.getBody())
                .extracting(f -> Tuple.tuple(f.pseudo))
                .containsExactly(
                        Tuple.tuple("ssgueye"),
                        Tuple.tuple("saliou"),
                        Tuple.tuple("dapieu"));

        verify(appUserService).getAllUsers();
    }

    private static AppUserEntity newUserEntity(String pseudo){
        AppUserEntity entity = new AppUserEntity();
        entity.pseudo = pseudo;

        return entity;
    }
}
