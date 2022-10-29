package com.uca.series_temporelles.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class AppUserTest {

    @Nested
    class UserSuccessCreationTests{
        @Test()
        void createUserWithLowerCaseLetters(){

            assertThat(new AppUser("pseudo")).isNotNull();

        }

        @Test
        void createUserWithUppercaseLetters(){

            assertThat(new AppUser("PSEUDO")).isNotNull();
        }

        @Test
        void VerifyIfPseudoIsConvertedIntoLowerCases(){
            //Given
            AppUser appUser = new AppUser("PSEUDO");

            //Then
            assertEquals("pseudo", appUser.pseudo);
        }


        @Test
        void createUserWithOnlyNumericCharacters(){

            assertThat(new AppUser("123456")).isNotNull();
        }

        @Test
        void createUserWithOnlySpecialCharacters(){
            assertThat(new AppUser("#_@'é$*%")).isNotNull();

        }
    }

    @Nested()
    class UserFailCreationTests{
        @Test
        void canNotCreateUserWithANullPseudo(){

            assertThrows(IllegalArgumentException.class, ()-> new AppUser(null));
        }

        @Test
        void canNotCreateUserWithAnEmptyPseudo(){

            assertThrows(IllegalArgumentException.class, ()-> new AppUser(""));
        }

        @Test
        void canNotCreateUserWithBlankPseudo(){

            assertThrows(IllegalArgumentException.class, ()-> new AppUser(" "));
        }

        @Test
        void canNotCreateUserWithPseudoContainingWhiteSpaces(){

            assertThrows(IllegalArgumentException.class, ()-> new AppUser("mon pseudo"));
        }
    }

    @Nested
    class SerializeAppUser{
        private final String JSON = "{\"pseudo\":\"test\"}";

        @Test
        void mustSerializeToJson() throws JsonProcessingException {
            AppUser appUser = new AppUser("test");
            var mapper = new ObjectMapper();

            assertThat(mapper.writeValueAsString(appUser)).isEqualTo(JSON);
        }

        @Test
        void mustDeserializeFromJson() throws JsonProcessingException {
            var mapper = new ObjectMapper();
            AppUser appUser = mapper.readValue(JSON, AppUser.class);

            assertThat(appUser).isNotNull();
            assertThat(appUser.pseudo).isEqualTo("test");

        }
    }

}
