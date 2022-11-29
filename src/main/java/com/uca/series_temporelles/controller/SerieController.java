package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.model.Event;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.UserSerie;
import com.uca.series_temporelles.repository.AppUserRepository;
import com.uca.series_temporelles.repository.EventRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
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
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserSerieRepository userSerieRepository;
    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/series")
    public List<Serie> getAllSeries(){
        return serieRepository.findAll();
    }
    @GetMapping("/serie/{id}")
    public Serie getSerie(@PathVariable("id") Long id){
        return serieRepository.findById(id).orElseThrow(()->new IllegalArgumentException());
    }
    @PostMapping("/user/{id}/create/serie")
    public ResponseEntity<Serie> saveSerie(@RequestBody Serie serie, @PathVariable("id") Long idUser){
        UserSerie userSerie = new UserSerie();
        serie.setLastUpdatedDate(LocalDateTime.now());
        Serie serieSaved = serieRepository.save(serie);
        String url = "/serie/"+serieSaved.getId();
        userSerie.setSerie(serieSaved);
        userSerie.setUser(appUserRepository.findById(idUser).orElseThrow(()-> new IllegalArgumentException()));
        userSerie.setOwner(true);
        userSerie.setPrivilege("Read & Write");
        userSerieRepository.save(userSerie);
        return ResponseEntity.created(URI.create(url)).body(serieSaved);
    }
    @DeleteMapping("/serie/{id}")
    public void deleteSerie(@PathVariable("id") Long id){
        serieRepository.deleteById(id);
    }
    @PutMapping("/user/{userId}/update/serie/{serieId}/title")
    public ResponseEntity<Serie> updateTitleOfSeri(String newTitle, @PathVariable("userId") Long userId,
                                                   @PathVariable("serieId") Long serieId){
        Serie serieUpdated = serieRepository.findById(serieId).orElseThrow(()-> new IllegalArgumentException());
        serieUpdated.setTitle(newTitle);
        serieUpdated.setLastUpdatedDate(LocalDateTime.now());
        String url = "/user/{userId}/update/serie/{serieId}/title="+serieUpdated.getTitle();
        serieRepository.save(serieUpdated);
        return ResponseEntity.created(URI.create(url)).body(serieUpdated);
    }

    @PutMapping("/user/{userId}/update/serie/{serieId}/description")
    public ResponseEntity<Serie> updateDescriptionOfSeri(String newDescription, @PathVariable("userId") Long userId,
                                                   @PathVariable("serieId") Long serieId){
        Serie serieUpdated = serieRepository.findById(serieId).orElseThrow(()-> new IllegalArgumentException());
        serieUpdated.setDescription(newDescription);
        serieUpdated.setLastUpdatedDate(LocalDateTime.now());
        String url = "/user/{userId}/update/serie/{serieId}/title="+serieUpdated.getDescription();
        serieRepository.save(serieUpdated);
        return ResponseEntity.created(URI.create(url)).body(serieUpdated);
    }

    @PutMapping("/user/{userId}/serie/{serieId}/add/event/{eventId}")
    public ResponseEntity<Serie> addEventToSerie(@PathVariable("userId") Long userId,
                                                 @PathVariable("serieId") Long serieId,
                                                 @PathVariable("eventId") Long eventId){
        AppUser user = appUserRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException());
        Serie serie = serieRepository.findById(serieId).orElseThrow(()-> new IllegalArgumentException());
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException());
        event.setSerie(serie);
        event.setLastUpdateDate(LocalDateTime.now());
        serie.setLastUpdatedDate(LocalDateTime.now());
        String url = "/user/{userId}/serie/{serieId}/add/event/{eventId}";
        eventRepository.save(event);
        serieRepository.save(serie);
        return ResponseEntity.created(URI.create(url)).body(serie);
    }

}
