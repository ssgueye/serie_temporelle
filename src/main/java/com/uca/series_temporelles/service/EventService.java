package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.EventEntity;
import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.Event;
import com.uca.series_temporelles.repository.EventRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SerieRepository serieRepository;
    private final UserSerieRepository userSerieRepository;

    public EventService(EventRepository eventRepository,
                        SerieRepository serieRepository,
                        UserSerieRepository userSerieRepository) {
        this.eventRepository = eventRepository;
        this.serieRepository = serieRepository;
        this.userSerieRepository = userSerieRepository;
    }

    public Iterable<EventEntity> getAllEventsForAGivenSerie(String pseudo, Long serie_id){

        Assert.hasText(pseudo, "pseudo can not be null/empty/blank");
        Assert.notNull(serie_id, "serie_id can not be null");

        UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
        //make sure that the User that has been linked to the Serie, only can see the list of events for that serie
        if(userSerieEntity != null){
            return StreamUtils.createStreamFromIterator(eventRepository.getAllEventsBySerieId(serie_id).iterator()).collect(Collectors.toList());
        }
        else{
            throw new NoAccessDataException("Can not access to the Serie "+serie_id);
        }

    }

    public EventEntity getOne(String pseudo, Long serie_id, Long event_id){

        Assert.hasText(pseudo, "pseudo can not be null/empty/blank");
        Assert.notNull(serie_id, "serie_id can not be null");
        Assert.notNull(event_id, "event_id can not be null");

        EventEntity event = eventRepository.findById(event_id).orElseThrow(()-> new ResourceNotFoundException("Resource Not found"));
        //Make sure the event that the user want to update belongs to that serie
        if(event.serie.id_serie.equals(serie_id)){

            //Make sure that the user is linked to that serie
            UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
            //Make sure that the user has the permission to update the event
            if(userSerieEntity!=null){

                return event;

            }
            else{
                throw new NoAccessDataException("Can not access to the Serie "+serie_id);
            }

        }
        else{
            throw new NoAccessDataException("Can not access to the event "+event_id);
        }
    }

    public Iterable<EventEntity> FilterEventsByTag(String pseudo,String label_tag){
        Assert.hasText(pseudo, "pseudo can not be null/empty/blank");
        Assert.hasText(label_tag, "label_tag can not be null/empty/blank");

            return StreamUtils.createStreamFromIterator(eventRepository.FilterEventsByTag(label_tag.toUpperCase(), pseudo).iterator()).collect(Collectors.toList());
    }

    public Integer TagFrequencyByDateRange(String label, String pseudo, LocalDateTime start_date, LocalDateTime end_date){
        Assert.hasText(pseudo, "pseudo can not be null/empty/blank");
        Assert.hasText(label, "label can not be null/empty/blank");

        return StreamUtils.createStreamFromIterator(eventRepository.getTagFrequencyByEventDateRange(label.toUpperCase(), pseudo, start_date, end_date).iterator()).collect(Collectors.toList()).size();
    }


    public EventEntity addEventToSerie(String pseudo, Long serie_id, Event event){

        Assert.hasText(pseudo, "pseudo can not be null/empty/blank");
        Assert.notNull(serie_id, "serie_id can not be null");
        Assert.notNull(event, "event can not be null");

        UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);

        if(userSerieEntity.isOwner || userSerieEntity.permission.equals(Permission.WRITE_READ)){
            SerieEntity serie = serieRepository.findById(serie_id).orElseThrow(()-> new ResourceNotFoundException("Ressource not found"));
            //Update the lastUpdatedDate for the serie
            EventEntity newEvent = new EventEntity();
            newEvent.event_date = event.event_date;
            newEvent.value = event.value;
            newEvent.comment = event.comment;
            newEvent.lastUpdatedDate = event.lastUpdatedDate;
            newEvent.serie = serie;
            serie.lastUpdatedDate = event.lastUpdatedDate;

            return eventRepository.save(newEvent);
        }
        else{
            throw new NoAccessDataException("Can not access to the Serie "+serie_id);
        }
    }

    public EventEntity updateEvent(String pseudo, Long serie_id, Long event_id, Event event){

        Assert.hasText(pseudo, "pseudo can not be null/empty/blank");
        Assert.notNull(serie_id, "serie_id can not be null");
        Assert.notNull(event_id, "event_id can not be null");
        Assert.notNull(event, "event can not be null");

        EventEntity updatedEvent = eventRepository.findById(event_id).orElseThrow(()-> new ResourceNotFoundException("Resource Not found"));
        //Make sure the event that the user want to update belongs to that serie
        if(updatedEvent.serie.id_serie.equals(serie_id)){

            //Make sure that the user is linked to that serie
            UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
            //Make sure that the user has the permission to update the event
            if(userSerieEntity.isOwner || userSerieEntity.permission.equals(Permission.WRITE_READ)){
                updatedEvent.event_date = event.event_date;
                updatedEvent.value = event.value;
                updatedEvent.comment = event.comment;
                updatedEvent.lastUpdatedDate = LocalDateTime.now();

                //Change lastUpdatedDate for the serie
                SerieEntity updatedSerie = serieRepository.findById(serie_id).orElseThrow(()-> new ResourceNotFoundException("Resource Not found"));
                updatedSerie.lastUpdatedDate = updatedEvent.lastUpdatedDate;

                return eventRepository.save(updatedEvent);

            }
            else{
                throw new NoAccessDataException("Doesn't have the rights to update events in Serie "+serie_id);
            }

        }
        else{
            throw new NoAccessDataException("Doesn't have the rights to update events in Serie "+event_id);
        }

    }

    public void delete(String pseudo, Long serie_id, Long event_id){

        try {

            EventEntity eventToDelete = eventRepository.findById(event_id).orElseThrow(()-> new ResourceNotFoundException("Resource Not found"));
            if(eventToDelete.serie.id_serie.equals(serie_id)){
                UserSerieEntity userSerieEntity = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
                if(userSerieEntity.isOwner){
                    eventRepository.delete(eventToDelete);
                    //When we delete an event, the serie that it was linked is changed, so we have to update it's lastUpdatedDate
                    SerieEntity serie = serieRepository.findById(serie_id).orElseThrow(()-> new ResourceNotFoundException("Resource Not found"));
                    serie.lastUpdatedDate = LocalDateTime.now();
                    serieRepository.save(serie);
                }
                else {
                    throw new NoAccessDataException("Only the Owner can delete this Serie "+serie_id);
                }
            }
            else {
                throw new NoAccessDataException("Can not access to the event "+event_id);

            }

        }catch (EmptyResultDataAccessException e){
            // We can safely Ignore this
        }
    }


}
