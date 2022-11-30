package com.uca.series_temporelles.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {


    @Nested
    class EventSuccessCreationTests{

        @Test
        void createEventWithOptionalComment(){

            Event event = new Event(LocalDateTime.now() ,7.7, null);
            assertThat(event).isNotNull();

            //Verify also that lastUpdatedDate are not null
            assertThat(event.lastUpdatedDate).isNotNull();

        }
    }

    @Nested
    class EventFailCreationTests{

        @Test
        void createEventWithNullOnValue(){
            assertThrows(IllegalArgumentException.class, ()-> new Event(LocalDateTime.now(),null, "comment"));
        }

    }
    

}
