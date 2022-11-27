package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.EventEntity;
import com.uca.series_temporelles.entity.TagEntity;
import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.Tag;
import com.uca.series_temporelles.repository.AppUserRepository;
import com.uca.series_temporelles.repository.EventRepository;
import com.uca.series_temporelles.repository.TagRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final EventRepository eventRepository;
    private final UserSerieRepository userSerieRepository;

    public TagService(TagRepository tagRepository, EventRepository eventRepository, UserSerieRepository userSerieRepository) {
        this.tagRepository = tagRepository;
        this.eventRepository = eventRepository;
        this.userSerieRepository = userSerieRepository;
    }

    public Iterable<TagEntity> listAllTagsByEvent(String pseudo, Long serie_id, Long event_id){
        EventEntity entity = eventRepository.findById(event_id).orElseThrow(()-> new ResourceNotFoundException("Eevnt not found"));
        //Verify if the event belongs to the serie
        if(entity.serie.id_serie.equals(serie_id)){
            UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);

            if(userSerieEntity.isOwner || userSerieEntity.permission.equals(Permission.WRITE_READ)){
                return StreamUtils.createStreamFromIterator(tagRepository
                        .findAll().iterator())
                        .collect(Collectors.toList());
            }
            else {
                throw new NoAccessDataException("User "+pseudo+" does not have the permission to access to this data");
            }
        }
        else {
            throw new ResourceNotFoundException("Can not found the event "+event_id+" in the serie "+serie_id);
        }
    }

    public TagEntity addTag(String pseudo, Long serie_id, Long event_id, Tag tag){
        EventEntity entity = eventRepository.findById(event_id).orElseThrow(()-> new ResourceNotFoundException("Eevnt not found"));
        if(entity.serie.id_serie.equals(serie_id)){
            UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
            if(userSerieEntity.isOwner || userSerieEntity.permission.equals(Permission.WRITE_READ)){
                TagEntity tagEntity = new TagEntity();
                tagEntity.event = entity;
                tagEntity.label = tag.label;

                return tagRepository.save(tagEntity);

            }
            else {
                throw new NoAccessDataException("User "+pseudo+" does not have the permission to access to this data");
            }
        }
        else {
            throw new ResourceNotFoundException("Can not found the event "+event_id+" in the serie "+serie_id);
        }
    }

    public TagEntity update(String pseudo, Long serie_id, Long event_id, Long tag_id, Tag tag){
        EventEntity entity = eventRepository.findById(event_id).orElseThrow(()-> new ResourceNotFoundException("Event not found"));
        if(entity.serie.id_serie.equals(serie_id)){
            UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
            if(userSerieEntity.isOwner || userSerieEntity.permission.equals(Permission.WRITE_READ)){
                TagEntity updatedTag = tagRepository.findById(tag_id).orElseThrow(()-> new ResourceNotFoundException("Tag not found"));
                if(updatedTag.event.id_event.equals(entity.id_event)){
                    updatedTag.label = tag.label;
                    return tagRepository.save(updatedTag);
                }
                else{
                    throw new NoAccessDataException("User "+pseudo+" does not have the permission to access to this tag");
                }
            }
            else {
                throw new NoAccessDataException("User "+pseudo+" does not have the permission to access to this serie");
            }
        }
        else {
            throw new ResourceNotFoundException("Can not found the event "+event_id+" in the serie "+serie_id);
        }
    }

    public void deleteTag(String pseudo, Long serie_id, Long event_id, Long tag_id){
        try{
            EventEntity entity = eventRepository.findById(event_id).orElseThrow(()-> new ResourceNotFoundException("Event not found"));
            if(entity.serie.id_serie.equals(serie_id)){
                UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
                if(userSerieEntity.isOwner){
                    TagEntity updatedTag = tagRepository.findById(tag_id).orElseThrow(()-> new ResourceNotFoundException("Tag not found"));
                    if(updatedTag.event.id_event.equals(entity.id_event)){
                        tagRepository.deleteById(tag_id);
                    }
                    else{
                        throw new NoAccessDataException("User "+pseudo+" does not have the permission to access to this tag");
                    }
                }
                else {
                    throw new NoAccessDataException("User "+pseudo+" does not have the permission to access to this serie");
                }
            }
            else {
                throw new ResourceNotFoundException("Can not found the event "+event_id+" in the serie "+serie_id);
            }
        }catch (EmptyResultDataAccessException exception){
            // We can safely ignore this
        }

    }
}
