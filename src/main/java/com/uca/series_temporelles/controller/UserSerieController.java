package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.UserSerie;
import com.uca.series_temporelles.repository.AppUserRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UserSerieController {

    @Autowired
    private UserSerieRepository userSerieRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private SerieRepository serieRepository;

    @PostMapping("/share/serie/{idSerie}/with/{idUser}")
    public ResponseEntity<UserSerie> shareSerie(@RequestBody UserSerie userSerie,
                                     @PathVariable("idSerie") Long idSerie,
                                     @PathVariable("idUser") Long idUser){
        userSerie.setUser(appUserRepository.findById(idUser).orElseThrow(()-> new IllegalArgumentException()));
        userSerie.setSerie(serieRepository.findById(idSerie).orElseThrow(()-> new IllegalArgumentException()));
        userSerie.setOwner(false);
        UserSerie userSerieSaved = userSerieRepository.save(userSerie);
        String url = "/user/serie/" + userSerieSaved.getId();
        return ResponseEntity.created(URI.create(url)).body(userSerieSaved);
    }
}
