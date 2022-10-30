package com.uca.series_temporelles.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uca.series_temporelles.enumerations.Privilege;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class UserSerieTest {

    @Nested
    class UserSerieSuccessCreationTests{
        @Test
        void createUserSerieWithAllArguments(){

            AppUser appUser = new AppUser("mon_pseudo");
            Serie serie = new Serie("my title", "my desc");
            var userSerie = new UserSerie(Privilege.READONLY, true, appUser, serie);

            assertThat(userSerie).isNotNull();
            assertEquals(Privilege.WRITE_READ, userSerie.privilege);//Testing if Privilege is "WRITE_READ" when isOwner is "True" and even A "READONLY" value is given
        }
    }

    @Nested
    class UserSerieFailCreationTests{
        @Test
        void canNotCreateUserSerieWithNullValueInPrivilege(){

            AppUser appUser = new AppUser("mon_pseudo");
            Serie serie = new Serie("my title", "my desc");

            assertThrows(IllegalArgumentException.class, ()-> new UserSerie(null, true, appUser, serie));
        }

        @Test
        void canNotCreateUserSerieWithNullValueInIsOwner(){

            AppUser appUser = new AppUser("mon_pseudo");
            Serie serie = new Serie("my title", "my desc");

            assertThrows(IllegalArgumentException.class, ()-> new UserSerie(Privilege.WRITE_READ, null, appUser, serie));
        }

        @Test
        void canNotCreateUserSerieWithNullValueInAppUser(){

            Serie serie = new Serie("my title", "my desc");
            assertThrows(IllegalArgumentException.class, ()-> new UserSerie(Privilege.WRITE_READ, true, null, serie));
        }

        @Test
        void canNotCreateUserSerieWithNullValueInSerie(){

            AppUser appUser = new AppUser("mon_pseudo");
            assertThrows(IllegalArgumentException.class, ()-> new UserSerie(Privilege.WRITE_READ, true, appUser, null));
        }
    }

    @Nested
    class SerializeUserSerie{

        @Test
        void mustSerializeToJson() throws JsonProcessingException {
            AppUser appUser = new AppUser("test");
            Serie serie = new Serie("my title", "my desc");
            UserSerie userSerie = new UserSerie(Privilege.WRITE_READ, true, appUser, serie);

            var mapper = new ObjectMapper();

            String JSON = "{\"privilege\":\"WRITE_READ\"}";
            assertThat(mapper.writeValueAsString(userSerie)).isEqualTo(JSON);
        }

    }

}
