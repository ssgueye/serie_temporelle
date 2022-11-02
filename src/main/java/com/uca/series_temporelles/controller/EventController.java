package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.Event;
import com.uca.series_temporelles.model.Tag;
import com.uca.series_temporelles.repository.EventRepository;
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

    @GetMapping("/events")
    public List<Event> getAllEvent(){
        return eventRepository.findAll();
    }
    @GetMapping("/event/{id}")
    public Event getEvent(@PathVariable Long id){
        return eventRepository.findById(id).get();
    }
    @PostMapping("/event")
    public ResponseEntity<Event> saveEvent(@RequestBody Event event){
        event.setLastUpdateDate(LocalDateTime.now());
        Event savedEvent = eventRepository.save(event);
        String url = "/event/"+savedEvent.getId();
        return ResponseEntity.created(URI.create(url)).body(savedEvent);
    }
    @DeleteMapping("/event/{id}")
    public void deleteEvent(@PathVariable Long id){
        eventRepository.deleteById(id);
    }

}
