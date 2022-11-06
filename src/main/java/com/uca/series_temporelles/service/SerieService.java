package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.model.Serie;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class SerieService {

    public Serie toSerie(SerieEntity serie){
        Assert.notNull(serie, "Serie can not be null");

        return new Serie(serie.title, serie.description);
    }

    public SerieEntity toSerieEntity(Serie serie){

        SerieEntity serieEntity = new SerieEntity();
        serieEntity.title = serie.title;
        serieEntity.description = serie.description;
        serieEntity.lastUpdatedDate = serie.lastUpdatedDate;

        return serieEntity;
    }

}
