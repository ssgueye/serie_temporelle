package com.uca.series_temporelles.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class SerieTest {

    @Nested
    public class SerieSuccessCreationTests{

        @Test
        public void createASerieWithTitleAndDescription(){
            Serie serie = new Serie("My new serie", "This is my new daily serie");
            assertThat(serie).isNotNull();

            //verify that lustupdatedDate is not null
            assertThat(serie.lastUpdatedDate).isNotNull();
        }

        @Test
        public void createASerieWithoutDescription(){
            assertThat(new Serie("My Serie without description", null)).isNotNull();
        }

    }

    @Nested
    public class SerieFailCreationTests{
        @Test
        public void canNotCreateSerieWithNullTitle(){
            assertThrows(NullPointerException.class, ()-> new Serie(null, "my desc"));
        }

        @Test
        public void canNotCreateASerieWithEmptyTitle(){
            assertThrows(IllegalArgumentException.class, ()-> new Serie("", "my desc"));
        }

        @Test
        public void canNotCreateASerieWithBlankTitle(){
            assertThrows(IllegalArgumentException.class, ()-> new Serie(" ", "my desc"));
        }

        @Test
        public void canNotCreateASerieWithTitleExceed50Characters(){
            String title = "Lorem Ipsum is simply dummy text of the printing" +
                    " and typesetting industry. Lorem Ipsum has been " +
                    "the industry's standard dummy text ever since the 1500s, when an unknown printer" +
                    " took a galley of type and scrambled it to make a type specimen book.";

            assertThrows(IllegalArgumentException.class, ()-> new Serie(title, "my description to a very long title"));
        }
    }

    @Nested
    class SerializeSerie{
        private final String JSON = "{\"title\":\"test\",\"description\":\"desc\"}";

        @Test
        void mustSerializeToJson() throws JsonProcessingException {
            Serie serie = new Serie("test", "desc");
            serie.lastUpdatedDate = LocalDateTime.now();
            var mapper = new ObjectMapper();

            assertThat(mapper.writeValueAsString(serie)).isEqualTo(JSON);
        }

        @Test
        void mustDeserializeFromJson() throws JsonProcessingException {
            var mapper = new ObjectMapper();
            Serie serie = mapper.readValue(JSON, Serie.class);

            assertThat(serie).isNotNull();
            assertThat(serie.title).isEqualTo("test");
            assertThat(serie.description).isEqualTo("desc");

        }
    }

}
