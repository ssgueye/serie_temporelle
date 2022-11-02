package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository serieRepository;

    @GetMapping("/series")
    public List<Serie> getAllSeries(){
        return serieRepository.findAll();
    }
    @GetMapping("/serie/{id}")
    public Serie getSerie(@PathVariable("id") Long id){
        return serieRepository.findById(id).orElseThrow(()->new IllegalArgumentException());
    }
    @PostMapping("/serie")
    public ResponseEntity<Serie> saveSerie(@RequestBody Serie serie){
        serie.setLastUpdatedDate(LocalDateTime.now());
        Serie serieSaved = serieRepository.save(serie);
        String url = "/serie/"+serieSaved.getId();
        return ResponseEntity.created(URI.create(url)).body(serieSaved);
    }
    @DeleteMapping("/serie/{id}")
    public void deleteSerie(@PathVariable("id") Long id){
        serieRepository.deleteById(id);
    }

}
