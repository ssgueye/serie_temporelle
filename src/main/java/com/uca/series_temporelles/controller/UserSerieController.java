package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.service.SerieService;
import com.uca.series_temporelles.service.UserSerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/user_series")
public class UserSerieController {

    private final UserSerieService userSerieService;
    private final SerieService serieService;

    public UserSerieController(UserSerieService userSerieService, SerieService serieService) {
        this.userSerieService = userSerieService;
        this.serieService = serieService;
    }

    @GetMapping("{pseudo}")
    public ResponseEntity<Iterable<UserSerieEntity>> getAllUserSeries(@PathVariable String pseudo){

        return ResponseEntity.ok(userSerieService.getAllUserSeries(pseudo));
    }

    @GetMapping("{pseudo}/{serie_id}")
    public ResponseEntity<UserSerieEntity> getOneUserSerie(@PathVariable("pseudo") String pseudo,
                                                     @PathVariable("serie_id") Long serie_id){

        if(!StringUtils.hasText(pseudo) || serie_id.equals(null)){
            return ResponseEntity.badRequest().build();
        }
        UserSerieEntity userSerie = userSerieService.getOne(pseudo, serie_id);
        if(userSerie == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userSerie);
    }

    @PostMapping(path = "add/{pseudo}", consumes = "application/json")
    public ResponseEntity<UserSerieEntity> createSerie(@PathVariable String pseudo, @RequestBody Serie serie){

        try {

            UserSerieEntity saved = userSerieService.createSerie(pseudo, serie);
            String uri  = "api/user_series/"+pseudo+"/"+saved.serie.id_serie;

            return ResponseEntity.created(URI.create(uri)).body(saved);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping(path = "share/{serie_id}/{pseudoOwner}/{pseudoReceiver}")
    public ResponseEntity<UserSerieEntity> shareSerie(@PathVariable("serie_id") Long serie_id,
                                                      @PathVariable("pseudoOwner") String pseudoOwner,
                                                      @PathVariable("pseudoReceiver") String pseudoReceiver,
                                                      @RequestParam("permission") Permission permission){
        try {

            UserSerieEntity share = userSerieService.shareSerie(serie_id, pseudoOwner, pseudoReceiver, permission);

            String uri  = "api/user_series/"+pseudoReceiver+"/"+share.serie.id_serie;

            return ResponseEntity.created(URI.create(uri)).body(share);
        }
        catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(path = "update/{serie_id}/{pseudo}", consumes = "application/json")
    public ResponseEntity<SerieEntity> updateSerie(@PathVariable("serie_id") Long serie_id,
                                                   @PathVariable("pseudo") String pseudo,
                                                   @RequestBody Serie serie){
        try {

            SerieEntity serieUpdated = userSerieService.updateSerie(serie_id, pseudo, serie);
            return ResponseEntity.ok(serieUpdated);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{serie_id}/{pseudo}")
    public ResponseEntity deleteSerie(@PathVariable("serie_id") Long serie_id,
                                            @PathVariable("pseudo") String pseudo){
        userSerieService.deleteSerie(serie_id, pseudo);

        return ResponseEntity.noContent().build();
    }
}
