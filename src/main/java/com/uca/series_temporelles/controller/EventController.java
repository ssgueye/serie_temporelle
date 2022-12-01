package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.EventEntity;
import com.uca.series_temporelles.model.Event;
import com.uca.series_temporelles.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("all")
    public ResponseEntity<Iterable<EventEntity>> getAllEventsForAGivenSerie(@RequestParam("pseudo") String pseudo,
                                                                            @RequestParam("serie_id") Long serie_id){
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, max-age=60, must-revalidate")
                .body(eventService.getAllEventsForAGivenSerie(pseudo, serie_id));
    }

    @GetMapping("one")
    public ResponseEntity<EventEntity> getOneEvent(@RequestParam("pseudo") String pseudo,
                                                   @RequestParam("serie_id") Long serie_id,
                                                   @RequestParam("event_id") Long event_id){
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, max-age=60, must-revalidate")
                .body(eventService.getOne(pseudo, serie_id, event_id));
    }

    @PostMapping("add")
    public ResponseEntity<EventEntity> addEventInASerie(@RequestParam("pseudo") String pseudo,
                                                        @RequestParam("serie_id") Long serie_id,
                                                        @RequestBody Event event){

        try{

            EventEntity saved = eventService.addEventToSerie(pseudo, serie_id, event);

            String uri = "api/events"+"/"+pseudo+"/"+serie_id+"/"+saved.id_event;

            return ResponseEntity.created(URI.create(uri)).body(saved);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("update")
    public ResponseEntity<EventEntity> updateAnEvent(@RequestParam("pseudo") String pseudo,
                                                     @RequestParam("serie_id") Long serie_id,
                                                     @RequestParam("event_id") Long event_id,
                                                     @RequestBody Event event){
        try {
            EventEntity updated = eventService.updateEvent(pseudo, serie_id, event_id, event);

            return ResponseEntity.ok(updated);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteEvent(@RequestParam("pseudo") String pseudo,
                                            @RequestParam("serie_id") Long serie_id,
                                            @RequestParam("event_id") Long event_id){

        eventService.delete(pseudo, serie_id, event_id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("filter/tag/{pseudo}")
    public ResponseEntity<Iterable<EventEntity>> FilterEventsByTag(@PathVariable("pseudo") String pseudo,
                                                                   @RequestParam String tag){
        return ResponseEntity.ok(eventService.FilterEventsByTag(pseudo, tag));
    }

    @GetMapping("frequency/tag/{pseudo}")
    public ResponseEntity<String> TagFrequencyByEvent(@PathVariable("pseudo") String pseudo,
                                                     @RequestParam("label") String tag,
                                                     @RequestParam("startDate")
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                     @RequestParam("endDate")
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date){
        return ResponseEntity.ok("Fréquence étiquette dans l'intervalle "+"["+startDate+";"+end_date+"] est de "+
                eventService.TagFrequencyByDateRange(tag, pseudo, startDate, end_date));
    }
}
