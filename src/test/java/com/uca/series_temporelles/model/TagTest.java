package com.uca.series_temporelles.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class TagTest {
/*
    private final static Serie serie = new Serie("Ma série", "Ma description");
    private final static Event event = new Event(
            LocalDateTime.of(2022, 11, 5, 10, 11),
            12.5, "comment", serie);
    @Nested
    class TagSuccessCreationTests{

        @Test
        void createATag(){
            assertThat(new Tag("tag1", event)).isNotNull();
        }
    }

    @Nested
    class TagFailCreationTests{

        @Test
        void canNotCreateATagWithNullLabel(){
            assertThrows(IllegalArgumentException.class, ()-> new Tag(null, event));
        }

        @Test
        void canNotCreateATagWithNullEvent(){
            assertThrows(IllegalArgumentException.class, ()-> new Tag("tag1", null));
        }
    }

    @Nested
    class SerializeTag{

        @Test
        void mustSerializeToJson() throws JsonProcessingException {
            Tag tag = new Tag("tag1", event);
            var mapper = new ObjectMapper();

            String JSON = "{\"label\":\"tag1\"}";
            assertThat(mapper.writeValueAsString(tag)).isEqualTo(JSON);
        }

        //Deserialization
        //TODO
    }
*/
}
