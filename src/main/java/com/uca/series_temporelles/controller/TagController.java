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

    @PostMapping("add")
    public ResponseEntity<TagEntity> addTagToAnEvent(@RequestParam("pseudo") String pseudo,
                                                     @RequestParam("serieId") Long serie_id,
                                                     @RequestParam("eventId") Long event_id,
                                                     @RequestBody Tag tag){
        try{
            TagEntity tagSaved = tagService.addTag(pseudo, serie_id, event_id, tag);
            String uri = "api/tags/"+"pseudo="+pseudo+"&serieId="+
                    serie_id+"&eventId="+event_id+"&tagId="+tagSaved.id;

            return ResponseEntity.created(URI.create(uri)).body(tagSaved);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("update")
    public ResponseEntity<TagEntity> updateTag(@RequestParam("pseudo") String pseudo,
                                                     @RequestParam("serieId") Long serie_id,
                                                     @RequestParam("eventId") Long event_id,
                                                     @RequestParam("tagId") Long tag_id,
                                                     @RequestBody Tag tag){
        TagEntity tagUpdated = tagService.update(pseudo, serie_id, event_id,tag_id, tag);

        return ResponseEntity.ok(tagUpdated);

    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> delete(@RequestParam("pseudo") String pseudo,
                                       @RequestParam("serieId") Long serie_id,
                                       @RequestParam("eventId") Long event_id,
                                       @RequestParam("tagId") Long tag_id){
        tagService.deleteTag(pseudo, serie_id, event_id, tag_id);

        return ResponseEntity.ok().build();
    }
}
