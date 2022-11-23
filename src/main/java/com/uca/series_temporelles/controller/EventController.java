package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.EventEntity;
import com.uca.series_temporelles.model.Event;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("{pseudo}/{serie_id}")
    public ResponseEntity<Iterable<EventEntity>> getAllEventsForAGivenSerie(@PathVariable("pseudo") String pseudo,
                                                                            @PathVariable("serie_id") Long serie_id){
        return ResponseEntity.ok(eventService.getAllEventsForAGivenSerie(pseudo, serie_id));
    }

    @GetMapping("{pseudo}/{serie_id}/{event_id}")
    public ResponseEntity<EventEntity> getOneEvent(@PathVariable("pseudo") String pseudo,
                                                   @PathVariable("serie_id") Long serie_id,
                                                   @PathVariable("event_id") Long event_id){
        return ResponseEntity.ok(eventService.getOne(pseudo, serie_id, event_id));
    }

    @PostMapping(path = "add/{pseudo}/{serie_id}", consumes = "application/json")
    public ResponseEntity<EventEntity> addEventInASerie(@PathVariable("pseudo") String pseudo,
                                                        @PathVariable("serie_id") Long serie_id,
                                                        @RequestBody Event event){

        try{

            EventEntity saved = eventService.addEventToSerie(pseudo, serie_id, event);

            String uri = "api/events"+"/"+pseudo+"/"+serie_id+"/"+saved.id_event;

            return ResponseEntity.created(URI.create(uri)).body(saved);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(path = "update/{pseudo}/{serie_id}/{event_id}", consumes = "application/json")
    public ResponseEntity<EventEntity> updateAnEvent(@PathVariable("pseudo") String pseudo,
                                                     @PathVariable("serie_id") Long serie_id,
                                                     @PathVariable("event_id") Long event_id,
                                                     @RequestBody Event event){
        try {
            EventEntity updated = eventService.updateEvent(pseudo, serie_id, event_id, event);

            return ResponseEntity.ok(updated);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{pseudo}/{serie_id}/{event_id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("pseudo") String pseudo,
                                            @PathVariable("serie_id") Long serie_id,
                                            @PathVariable("event_id") Long event_id){

        eventService.delete(pseudo, serie_id, event_id);
        return ResponseEntity.noContent().build();
    }
}
