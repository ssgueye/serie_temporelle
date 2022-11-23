package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.service.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/series")
public class SerieController {

    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping("OwnSeries/{pseudo}")
    public ResponseEntity<Iterable<SerieEntity>> getAllOwnSeries(@PathVariable String pseudo){
        return ResponseEntity.ok(serieService.getAllOwnSeries(pseudo));
    }

    @GetMapping("sharedSeries/{pseudo}")
    public ResponseEntity<Iterable<SerieEntity>> getAllSharedSeries(@PathVariable String pseudo){
        return ResponseEntity.ok(serieService.getAllSharedSeries(pseudo));
    }

    @GetMapping("/{pseudo}")
    public ResponseEntity<Iterable<SerieEntity>> getAllSeries(@PathVariable String pseudo){
        return ResponseEntity.ok(serieService.getAllSeries(pseudo));
    }

    @PostMapping(path = "add/{pseudo}", consumes = "application/json")
    public ResponseEntity<SerieEntity> createSerie(@PathVariable String pseudo, @RequestBody Serie serie){

        try {

            SerieEntity saved = serieService.createSerie(pseudo, serie);
            String uri  = "api/user_series/"+pseudo+"/"+saved.id_serie;

            return ResponseEntity.created(URI.create(uri)).body(saved);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(path = "update/{serie_id}/{pseudo}", consumes = "application/json")
    public ResponseEntity<SerieEntity> updateSerie(@PathVariable("serie_id") Long serie_id,
                                                   @PathVariable("pseudo") String pseudo,
                                                   @RequestBody Serie serie){
        try {

            SerieEntity serieUpdated = serieService.updateSerie(serie_id, pseudo, serie);
            return ResponseEntity.ok(serieUpdated);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{serie_id}/{pseudo}")
    public ResponseEntity<Void> deleteSerie(@PathVariable("serie_id") Long serie_id,
                                      @PathVariable("pseudo") String pseudo){
        serieService.deleteSerie(serie_id, pseudo);

        return ResponseEntity.noContent().build();
    }
}
