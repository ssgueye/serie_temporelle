package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.service.UserSerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/user_series")
public class UserSerieController {

    private final UserSerieService userSerieService;

    public UserSerieController(UserSerieService userSerieService) {
        this.userSerieService = userSerieService;
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

}
