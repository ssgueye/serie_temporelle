package com.uca.series_temporelles.service;

import com.uca.series_temporelles.repository.EventRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class EventSerieTest {

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventService eventService;


}
