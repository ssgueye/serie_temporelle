package com.uca.series_temporelles.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import com.uca.series_temporelles.entity.SerieEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SerieServiceTest {

    @InjectMocks
    SerieService serieService;

    @Test
    void canNotConvertNullSerieEntity(){
        assertThrows(IllegalArgumentException.class, ()-> serieService.toSerie(null));
    }

    @Test
    void canNotConvertSerieEntityWithoutATitle(){
        assertThrows(NullPointerException.class, ()-> serieService.toSerie(new SerieEntity()));
    }

    @Test
    void canConvertSerieEntity(){
        SerieEntity serieEntity = new SerieEntity();
        serieEntity.title = "Course";

        assertThat(serieService.toSerie(serieEntity).title).isEqualTo("Course");
        assertThat(serieService.toSerie(serieEntity).description).isEqualTo(null);
        assertThat(serieService.toSerie(serieEntity).lastUpdatedDate).isNotNull();
    }
}
