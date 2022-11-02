package com.uca.series_temporelles.service;

import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;

    public List<Serie> getAllSeries(){
        List<Serie> series = new ArrayList<>();
        serieRepository.findAll().forEach(serie -> series.add(serie));
        return series;
    }

    public Serie getSerie(Long id){
        return serieRepository.findById(id).get();
    }

    public void deleteSerie(Long id){
        serieRepository.deleteById(id);
    }

    public void saveSerie(Serie serie){
        serieRepository.save(serie);
    }

}
