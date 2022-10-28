package com.uca.series_temporelles.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class AppUserTest {

    @Nested
    public class successCreationTest{
        @Test()
        public void createUserWithLowerCaseLetters(){
            //Given
            AppUser appUser = new AppUser("pseudo");

            //Then
            assertThat(appUser);
        }

        @Test
        public void createUserWithUppercaseLetters(){
            //Given
            AppUser appUser = new AppUser("PSEUDO");

            //Then
            assertThat(appUser);
        }


        @Test
        public void createUserWithOnlyNumericCharacters(){
            //Given
            AppUser appUser = new AppUser("123456");

            //Then
            assertThat(appUser);
        }

        @Test
        public void createUserWithOnlySpecialCharacters(){
            //Given
            AppUser appUser = new AppUser("#_@'é$*%");

            //Then
            assertThat(appUser);
        }
    }

    @Nested()
    public class failCreationTest{
        @Test
        public void createUserWithANullPseudo(){

            //Given
            AppUser appUser = new AppUser();

            //Then
            assertThrows(IllegalArgumentException.class, ()-> appUser.setPseudo(null));
        }

        @Test
        public void createUserWithAnEmptyPseudo(){

            //Given
            AppUser appUser = new AppUser();

            //Then
            assertThrows(IllegalArgumentException.class, ()-> appUser.setPseudo(""));
        }

        @Test
        public void createUserWithBlankPseudo(){

            //Given
            AppUser appUser = new AppUser();

            //Then
            assertThrows(IllegalArgumentException.class, ()-> appUser.setPseudo(" "));
        }

        @Test
        public void createUserWithPseudoContainingWhiteSpaces(){

            //Given
            AppUser appUser = new AppUser();

            //Then
            assertThrows(IllegalArgumentException.class, ()-> appUser.setPseudo("mon pseudo"));
        }
    }


}
