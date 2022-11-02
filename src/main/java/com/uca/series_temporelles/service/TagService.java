package com.uca.series_temporelles.service;

import com.uca.series_temporelles.model.Tag;
import com.uca.series_temporelles.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTag(){
        List<Tag> tags = new ArrayList<Tag>();
        tagRepository.findAll().forEach(tag -> tags.add(tag));
        return tags;
    }
    public Tag getTag(Long id){
        return tagRepository.findById(id).get();
    }
    public void saveTag(Tag tag){
        tagRepository.save(tag);
    }
    public void deleteTag(Long id){
        tagRepository.deleteById(id);
    }
}
