package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.Tag;
import com.uca.series_temporelles.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TagController {

    @Autowired
    public TagRepository tagRepository;

    @GetMapping("/tags")
    public List<Tag> getAllTag(){
        return tagRepository.findAll();
    }
    @GetMapping("/tag/{id}")
    public Tag getTag(@PathVariable Long id){
        return tagRepository.findById(id).get();
    }
    @PostMapping("/tag")
    public Tag saveTag(@RequestBody Tag tag){
        return tagRepository.save(tag);
    }
    @DeleteMapping("/tag/{id}")
    public void deleteTag(@PathVariable Long id){
        tagRepository.deleteById(id);
    }
}
