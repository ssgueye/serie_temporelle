package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.TagEntity;
import com.uca.series_temporelles.model.Tag;
import com.uca.series_temporelles.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<Iterable<TagEntity>> getAllTagsByEvent(@RequestParam("pseudo") String pseudo,
                                                                 @RequestParam("serieId") Long serie_id,
                                                                 @RequestParam("eventId") Long event_id){

        return ResponseEntity.ok(tagService.listAllTagsByEvent(pseudo, serie_id, event_id));
    }

    @PostMapping(path = "add", consumes = "application/json")
    public ResponseEntity<TagEntity> addTagToAnEvent(@RequestParam("pseudo") String pseudo,
                                                     @RequestParam("serieId") Long serie_id,
                                                     @RequestParam("eventId") Long event_id,
                                                     @RequestBody Tag tag){
        try{
            TagEntity tagSaved = tagService.addTag(pseudo, serie_id, event_id, tag);
            String uri = "api/tags/"+"pseudo="+pseudo+"&serieId="+
                    serie_id+"&eventId="+event_id+"&tag="+tagSaved.label;

            return ResponseEntity.created(URI.create(uri)).body(tagSaved);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }
    }
}
