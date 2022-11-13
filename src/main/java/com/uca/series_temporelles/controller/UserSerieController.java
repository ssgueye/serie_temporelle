package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.model.Serie;
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
import java.util.List;

@RestController
public class UserSerieController {

    @Autowired
    private UserSerieRepository userSerieRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private SerieRepository serieRepository;

    @PostMapping("user/{idUser1}/share/serie/{idSerie}/with/{idUser2}")
    public ResponseEntity<UserSerie> shareSerie(@RequestBody UserSerie userSerie,
                                     @PathVariable("idSerie") Long idSerie,
                                     @PathVariable("idUser1") Long idUser1,
                                     @PathVariable("idUser2") Long idUser2){
        AppUser owner = appUserRepository.findById(idUser1).orElseThrow(()-> new IllegalArgumentException());
        AppUser nonOwner = appUserRepository.findById(idUser2).orElseThrow(()-> new IllegalArgumentException());
        Serie serie= serieRepository.findById(idSerie).orElseThrow(()-> new IllegalArgumentException());
        if (owner.getId() != nonOwner.getId() && isSerieOwner(owner,serie)){
            userSerie.setUser(nonOwner);
            userSerie.setSerie(serie);
            userSerie.setOwner(false);
            UserSerie userSerieSaved = userSerieRepository.save(userSerie);
            String url = "/user/serie/" + userSerieSaved.getId();
            return ResponseEntity.created(URI.create(url)).body(userSerieSaved);
        }
        return ResponseEntity.badRequest().body(userSerie);
    }
    
    
    public Boolean isSerieOwner(AppUser user, Serie serie){
        List<UserSerie> userSeries = userSerieRepository.findAll();
        for (UserSerie userSerie: userSeries) {
            if (userSerie.getUser() == user && userSerie.getSerie() == serie && userSerie.isOwner() == true){
                return true;
            }
        }
        return false;
    }
    public Boolean isAlreadyShared(AppUser user, Serie serie){
        return false;
    }
}
