package com.uca.series_temporelles.service;

import com.uca.series_temporelles.model.Event;
import com.uca.series_temporelles.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvent(){
        List<Event> events = new ArrayList<Event>();
        eventRepository.findAll().forEach(event ->events.add(event));
        return events;
    }
    public Event getEvent(Long id){
        return eventRepository.findById(id).get();
    }
    public void saveEvent(Event event){
        eventRepository.save(event);
    }
    public void deleteEvent(Long id){
        eventRepository.deleteById(id);
    }
}
