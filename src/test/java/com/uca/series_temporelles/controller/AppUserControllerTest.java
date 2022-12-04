package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.service.AppUserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserControllerTest {

    @Mock
    AppUserService appUserService;

    @InjectMocks
    AppUserController appUserController;


    @Nested
    class TestForUserControllerGetMethod{
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
            assertThat(appUsers).isNotNull();
            assertThat(appUsers.getBody())
                    .extracting(f -> Tuple.tuple(f.pseudo))
                    .containsExactly(
                            Tuple.tuple("ssgueye"),
                            Tuple.tuple("saliou"),
                            Tuple.tuple("dapieu"));

            verify(appUserService).getAllUsers();
        }

        @Test
        void mustReturnAnExistingUser(){
            var userEntity = newUserEntity("dapieu");

            when(appUserService.getOne(any())).thenReturn(userEntity);

            var appUser = appUserController.getOne("dapieu");

            assertThat(appUser.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(appUser).isNotNull();
            assertThat(appUser.getBody()).isNotNull();
            assertThat(appUser.getBody().pseudo).isEqualTo("dapieu");

            verify(appUserService).getOne(eq("dapieu"));
        }

        @Test
        void mustReturnBadRequestWithNullEmptyOrBlankPseudo(){

            var appUserNull = appUserController.getOne(null);
            assertThat(appUserNull.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(appUserNull).isNotNull();
            assertThat(appUserNull.getBody()).isNull();


            var appUserEmpty = appUserController.getOne("");
            assertThat(appUserEmpty.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(appUserEmpty).isNotNull();
            assertThat(appUserEmpty.getBody()).isNull();

            var appUserBlank = appUserController.getOne(" ");
            assertThat(appUserBlank.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(appUserBlank).isNotNull();
            assertThat(appUserBlank.getBody()).isNull();

            var appUserWhiteSpace = appUserController.getOne("saliou gueye");
            assertThat(appUserWhiteSpace.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(appUserWhiteSpace).isNotNull();
            assertThat(appUserWhiteSpace.getBody()).isNull();


            verify(appUserService, times(0)).getOne(any());
        }

    }

    @Nested
    class TestUserPostMethods{

        @Test
        void mustReturnBadRequestWithoutUser(){
            var appUserNull = appUserController.create(null);
            assertThat(appUserNull.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(appUserNull).isNotNull();
            assertThat(appUserNull.getBody()).isNull();

        }

        @Test
        void mustReturn201LocationWhenCreatingFeature() {
            when(appUserService.save(any())).thenReturn(newUserEntity("ssgueye"));

            var response = appUserController.create(new AppUser("ssgueye"));

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertNotNull(response.getHeaders().getLocation());
            assertThat(response.getHeaders().getLocation().toString()).isEqualTo("api/users/ssgueye");
            assertThat(response.getBody()).isNotNull();

            var captor = ArgumentCaptor.forClass(AppUser.class);
            verify(appUserService).save(captor.capture());
            var user = captor.getValue();
            assertThat(user.pseudo).isEqualTo("ssgueye");
        }

    }


    private static AppUserEntity newUserEntity(String pseudo){
        AppUserEntity entity = new AppUserEntity();
        entity.pseudo = pseudo;

        return entity;
    }


}
