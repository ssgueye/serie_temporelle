package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.Event;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.Tag;
import com.uca.series_temporelles.repository.EventRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    public EventRepository eventRepository;
    @Autowired
    public SerieRepository serieRepository;

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvent(){
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @GetMapping("/event/{id}")
    public Event getEvent(@PathVariable Long id){
        return eventRepository.findById(id).get();
    }

    @PostMapping("/serie/{idSerie}/add/event")
    public ResponseEntity<Event> saveEvent(@RequestBody Event event,
                                           @PathVariable("idSerie") Long idSerie){
        LocalDateTime now = LocalDateTime.now();
        Serie serie = serieRepository.findById(idSerie).orElseThrow(()-> new IllegalArgumentException());
        serie.setLastUpdatedDate(now);
        event.setLastUpdateDate(now);
        event.setSerie(serie);
        Event savedEvent = eventRepository.save(event);
        String url = "/event/"+savedEvent.getId();
        return ResponseEntity.created(URI.create(url)).body(savedEvent);
    }

    @DeleteMapping("/event/{id}")
    public void deleteEvent(@PathVariable Long id){
        eventRepository.deleteById(id);
    }

}
