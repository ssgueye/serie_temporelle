package com.uca.series_temporelles.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uca.series_temporelles.enumerations.Permission;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class UserSerieTest {

    private final static AppUser appUser = new AppUser("mon_pseudo");
    private final static Serie serie = new Serie("my title", "my desc");

    @Nested
    class UserSerieSuccessCreationTests{
        @Test
        void createUserSerieWithAllArguments(){

            var userSerie = new UserSerie(Permission.READONLY, true, appUser, serie);

            assertThat(userSerie).isNotNull();
            assertEquals(Permission.WRITE_READ, userSerie.permission);//Testing if Permission is "WRITE_READ" when isOwner is "True" and even A "READONLY" value is given
        }
    }

    @Nested
    class UserSerieFailCreationTests{
        @Test
        void canNotCreateUserSerieWithNullValueInPermission(){

            assertThrows(IllegalArgumentException.class, ()-> new UserSerie(null, true, appUser, serie));
        }

        @Test
        void canNotCreateUserSerieWithNullValueInIsOwner(){

            assertThrows(IllegalArgumentException.class, ()-> new UserSerie(Permission.WRITE_READ, null, appUser, serie));
        }

        @Test
        void canNotCreateUserSerieWithNullValueInAppUser(){

            assertThrows(IllegalArgumentException.class, ()-> new UserSerie(Permission.WRITE_READ, true, null, serie));
        }

        @Test
        void canNotCreateUserSerieWithNullValueInSerie(){

            assertThrows(IllegalArgumentException.class, ()-> new UserSerie(Permission.WRITE_READ, true, appUser, null));
        }
    }

    @Nested
    class SerializeUserSerie{

        private final static String JSON = "{\"permission\":\"WRITE_READ\"}";
        @Test
        void mustSerializeToJson() throws JsonProcessingException {

            UserSerie userSerie = new UserSerie(Permission.WRITE_READ, true, appUser, serie);

            var mapper = new ObjectMapper();


            assertThat(mapper.writeValueAsString(userSerie)).isEqualTo(JSON);
        }

        //Deserialization
        //TODO

    }

}
