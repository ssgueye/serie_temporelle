package com.uca.series_temporelles.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
/*
    private final static Serie serie = new Serie("Sport", "My Daily Sport");

    @Nested
    class EventSuccessCreationTests{

        @Test
        void createEventWithOptionalComment(){

            Event event = new Event(LocalDateTime.now() ,7.7, null, serie);
            assertThat(event).isNotNull();

            //Verify also that lastUpdatedDate are not null
            assertThat(event.lastUpdatedDate).isNotNull();

        }
    }

    @Nested
    class EventFailCreationTests{

        @Test
        void createEventWithNullOnValue(){
            assertThrows(IllegalArgumentException.class, ()-> new Event(LocalDateTime.now(),null, "comment", serie));
        }

        @Test
        void createEventWithNullSerie(){
            assertThrows(IllegalArgumentException.class, ()-> new Event(LocalDateTime.now(), 12.4, "comment", null));
        }
    }

    @Nested
    class SerializeEvent{
        private final static LocalDateTime date = LocalDateTime.of(2022, 11, 2, 9, 33);

        @Test
        void mustSerializeToJson() throws JsonProcessingException {
            Event event = new Event(date,12.5, "My comment", serie);
            var mapper = new ObjectMapper();

            String JSON = "{\"event_date\":\"02/11/2022 09:33\",\"value\":12.5,\"comment\":\"My comment\"}";
            assertThat(mapper.writeValueAsString(event)).isEqualTo(JSON);
        }

        //Deserialization
        //TODO
    }
*/
}
