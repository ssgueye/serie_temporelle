package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.entity.EventEntity;
import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.model.Event;
import com.uca.series_temporelles.repository.EventRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EventSerieTest {

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventService eventService;

    @Mock
    private UserSerieRepository userSerieRepository;
    @Mock
    private SerieRepository serieRepository;

    private final static LocalDateTime date = LocalDateTime.of(2022, 12, 2, 19, 59);

    @Nested
    class TestForGettingEvents{

        @Test
        void canNotGetAllExistingEventsWithIllegalArguments(){

            assertThrows(IllegalArgumentException.class, ()-> eventService.getAllEventsForAGivenSerie(null, 1L));
            assertThrows(IllegalArgumentException.class, ()-> eventService.getAllEventsForAGivenSerie("", 1L));
            assertThrows(IllegalArgumentException.class, ()-> eventService.getAllEventsForAGivenSerie(" ", 1L));
            assertThrows(IllegalArgumentException.class, ()-> eventService.getAllEventsForAGivenSerie("ssgueye", null));
        }

        @Test
        void canNotAccessToAllExistingUsersBecauseOfPermissions(){

            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(null);
            assertThrows(NoAccessDataException.class, ()-> eventService.getAllEventsForAGivenSerie("ssgueye", 1L));

        }

        @Test
        void mustReturnAllExistingEventsForOwners(){
            var user = newUserEntity("ssgueye");
            var serie = newSerieEntity(1L, "title1", "desc");

            var events = new EventEntity[]{
                    newEventEntity(1L, date, 3D, null, LocalDateTime.now(), serie),
                    newEventEntity(2L, date, 4D, null, LocalDateTime.now(), serie),
                    newEventEntity(3L, date, 12D, null, LocalDateTime.now(), serie),
            };
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true,user, serie));
            when(eventRepository.getAllEventsBySerieId(any())).thenReturn(Arrays.asList(events));

            var result = eventService.getAllEventsForAGivenSerie("ssgueye", 1L);
            assertThat(result).isNotNull();
            assertThat(result).doesNotContainNull();
            assertThat(result).extracting(t-> Tuple.tuple(t.id_event, t.event_date, t.value, t.comment, t.serie))
                    .containsExactly(Tuple.tuple(1L, date, 3D, null, serie),
                            Tuple.tuple(2L, date, 4D, null, serie),
                            Tuple.tuple(3L, date, 12D, null, serie));
            verify(eventRepository).getAllEventsBySerieId(eq(1L));
        }

        @Test
        void mustReturnAllExistingEventsForNoOwners(){
            var user = newUserEntity("dapieu");
            var serie = newSerieEntity(1L, "title1", "desc");

            var events = new EventEntity[]{
                    newEventEntity(1L, date, 3D, null, LocalDateTime.now(), serie),
                    newEventEntity(2L, date, 4D, null, LocalDateTime.now(), serie),
                    newEventEntity(3L, date, 12D, null, LocalDateTime.now(), serie),
            };
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, false,user, serie));
            when(eventRepository.getAllEventsBySerieId(any())).thenReturn(Arrays.asList(events));

            var result = eventService.getAllEventsForAGivenSerie("dapieu", 1L);
            assertThat(result).isNotNull();
            assertThat(result).doesNotContainNull();
            assertThat(result).extracting(t-> Tuple.tuple(t.id_event, t.event_date, t.value, t.comment, t.serie))
                    .containsExactly(Tuple.tuple(1L, date, 3D, null, serie),
                            Tuple.tuple(2L, date, 4D, null, serie),
                            Tuple.tuple(3L, date, 12D, null, serie));
            verify(eventRepository).getAllEventsBySerieId(eq(1L));
        }


        @Test
        void canNotGetAnExistingEventsWithIllegalArguments(){

            assertThrows(IllegalArgumentException.class, ()-> eventService.getOne(null, 1L, 1L));
            assertThrows(IllegalArgumentException.class, ()-> eventService.getOne("", 1L, 1L));
            assertThrows(IllegalArgumentException.class, ()-> eventService.getOne(" ", 1L, 1L));
            assertThrows(IllegalArgumentException.class, ()-> eventService.getOne("ssgueye", null, 1L));
            assertThrows(IllegalArgumentException.class, ()-> eventService.getOne("ssgueye", 1L, null));
        }

        @Test
        void canNotAccessToAnExistingUsersBecauseOfPermissions01(){
            var sameSerie = newSerieEntity(1L, "title1", "desc");

            when(eventRepository.findById(any())).thenReturn(Optional.of(newEventEntity(1L, date, 3D, null, LocalDateTime.now(), sameSerie)));
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(null);
            assertThrows(NoAccessDataException.class, ()-> eventService.getOne("ssgueye", 1L, 1L));

        }
        @Test
        void canNotAccessToAnExistingUsersBecauseOfPermissions02(){
            var differentSerie = newSerieEntity(2L, "title1", "desc");

            when(eventRepository.findById(any())).thenReturn(Optional.of(newEventEntity(1L, date, 3D, null, LocalDateTime.now(), differentSerie)));
            assertThrows(NoAccessDataException.class, ()-> eventService.getOne("ssgueye", 1L, 1L));

        }

        @Test
        void mustReturnAnExistingEventsForOwners(){
            var user = newUserEntity("ssgueye");
            var serie = newSerieEntity(1L, "title1", "desc");

            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true,user, serie));
            when(eventRepository.findById(any())).thenReturn(Optional.of(newEventEntity(1L, date, 3D, null, LocalDateTime.now(), serie)));

            var result = eventService.getOne("ssgueye", 1L, 1L);
            assertThat(result).isNotNull();
            assertThat(result.id_event).isEqualTo(1L);
            assertThat(result.event_date).isEqualTo(date);
            assertThat(result.value).isEqualTo(3D);
            assertThat(result.comment).isEqualTo(null);
            assertThat(result.serie).isEqualTo(serie);

            verify(eventRepository).findById(eq(1L));
        }

        @Test
        void mustReturnAnExistingEventsForNoOwners(){
            var user = newUserEntity("ssgueye");
            var serie = newSerieEntity(1L, "title1", "desc");

            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, false,user, serie));
            when(eventRepository.findById(any())).thenReturn(Optional.of(newEventEntity(1L, date, 3D, null, LocalDateTime.now(), serie)));

            var result = eventService.getOne("ssgueye", 1L, 1L);
            assertThat(result).isNotNull();
            assertThat(result.id_event).isEqualTo(1L);
            assertThat(result.event_date).isEqualTo(date);
            assertThat(result.value).isEqualTo(3D);
            assertThat(result.comment).isEqualTo(null);
            assertThat(result.serie).isEqualTo(serie);

            verify(eventRepository).findById(eq(1L));
        }

    }

    @Nested
    class TestEventsFiltersByTags{

        @Test
        void canNotFilterEventsBecauseOfIllegalArguments(){
            assertThrows(IllegalArgumentException.class, ()-> eventService.FilterEventsByTag(" ", "FROID"));
            assertThrows(IllegalArgumentException.class, ()-> eventService.FilterEventsByTag("", "FROID"));
            assertThrows(IllegalArgumentException.class, ()-> eventService.FilterEventsByTag("ssgueye", ""));
            assertThrows(IllegalArgumentException.class, ()-> eventService.FilterEventsByTag("ssgueye", " "));
            assertThrows(IllegalArgumentException.class, ()-> eventService.FilterEventsByTag("ssgueye", null));

        }
        @Test
        void mustFilterEvents(){
            var user = newUserEntity("ssgueye");
            var serie = newSerieEntity(1L, "title1", "desc");

            var events = new EventEntity[]{
                    newEventEntity(1L, date, 3D, null, LocalDateTime.now(), serie),
                    newEventEntity(2L, date, 4D, null, LocalDateTime.now(), serie),
                    newEventEntity(3L, date, 12D, null, LocalDateTime.now(), serie),
            };

            when(eventRepository.FilterEventsByTag(any(), any())).thenReturn(Arrays.asList(events));


            var result = eventService.FilterEventsByTag("ssgueye", "FROID");
            assertThat(result).isNotNull();
            assertThat(result).doesNotContainNull();
            assertThat(result).extracting(t-> Tuple.tuple(t.id_event, t.event_date, t.value, t.comment, t.serie))
                    .containsExactly(Tuple.tuple(1L, date, 3D, null, serie),
                            Tuple.tuple(2L, date, 4D, null, serie),
                            Tuple.tuple(3L, date, 12D, null, serie));
            verify(eventRepository).FilterEventsByTag(any(), any());

        }
    }

    @Nested
    class TestTagFrequencies{

        private final static LocalDateTime startDate = LocalDateTime.of(2022, 12, 1, 19, 59);
        private final static LocalDateTime endDate = LocalDateTime.of(2021, 12, 1, 00, 00);

        @Test
        void canNotGetTagFrequencyBecauseOfIllegalArguments(){
            assertThrows(IllegalArgumentException.class, ()-> eventService.TagFrequencyByDateRange(null, "ssgueye", startDate, endDate));
            assertThrows(IllegalArgumentException.class, ()-> eventService.TagFrequencyByDateRange("", "ssgueye", startDate, endDate));
            assertThrows(IllegalArgumentException.class, ()-> eventService.TagFrequencyByDateRange(" ", "ssgueye", startDate, endDate));
            assertThrows(IllegalArgumentException.class, ()-> eventService.TagFrequencyByDateRange("FROID", "", startDate, endDate));
            assertThrows(IllegalArgumentException.class, ()-> eventService.TagFrequencyByDateRange("FROID", " ", startDate, endDate));
            assertThrows(IllegalArgumentException.class, ()-> eventService.TagFrequencyByDateRange("FROID", null, startDate, endDate));

        }
        @Test
        void mustGetTagFrequency(){
            var user = newUserEntity("ssgueye");
            var serie = newSerieEntity(1L, "title1", "desc");

            var events = new EventEntity[]{
                    newEventEntity(1L, date, 3D, null, LocalDateTime.now(), serie),
                    newEventEntity(2L, date, 4D, null, LocalDateTime.now(), serie),
                    newEventEntity(3L, date, 12D, null, LocalDateTime.now(), serie),
            };

            when(eventRepository.getTagFrequencyByEventDateRange(any(), any(), any(), any())).thenReturn(Arrays.asList(events));


            var result = eventService.TagFrequencyByDateRange("FROID", "ssgueye", startDate, endDate);
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(3); //because there are 2 events here
            verify(eventRepository).getTagFrequencyByEventDateRange(any(), any(), any(), any());

        }
    }

    @Nested
    class TestCreateEvent{

        @Test
        void canNotCreateEventBecauseOfIllegalArguments(){
            assertThrows(IllegalArgumentException.class, ()-> eventService
                    .addEventToSerie(null, 1L, new Event( date, 3D, null)));
            assertThrows(IllegalArgumentException.class, ()-> eventService
                    .addEventToSerie(" ", 1L, new Event( date, 3D, null)));
            assertThrows(IllegalArgumentException.class, ()-> eventService
                    .addEventToSerie("", 1L, new Event( date, 3D, null)));
            assertThrows(IllegalArgumentException.class, ()-> eventService
                    .addEventToSerie("ssgueye", null, new Event( date, 3D, null)));
            assertThrows(IllegalArgumentException.class, ()-> eventService
                    .addEventToSerie("ssgueye", 1L, null));
        }
    }


    private static UserSerieEntity newUserSerieEntity(Long id,
                                                      Permission permission,
                                                      Boolean isOwner,
                                                      AppUserEntity userEntity,
                                                      SerieEntity serieEntity){
        UserSerieEntity entity = new UserSerieEntity();
        entity.id_user_serie = id;
        entity.permission = permission;
        entity.isOwner = isOwner;
        entity.serie = serieEntity;
        entity.appUser = userEntity;

        return entity;
    }

    private SerieEntity newSerieEntity(Long id, String title, String desc){
        SerieEntity serie = new SerieEntity();
        serie.id_serie = id;
        serie.title = title;
        serie.description = desc;
        serie.lastUpdatedDate = date;

        return serie;
    }

    private static AppUserEntity newUserEntity(String pseudo){
        AppUserEntity entity = new AppUserEntity();
        entity.pseudo = pseudo;

        return entity;
    }

    private static EventEntity newEventEntity(Long id, LocalDateTime event_date,
                                              Double value, String comment,
                                              LocalDateTime lastUpdatedDate, SerieEntity serie){
        EventEntity event = new EventEntity();
        event.id_event = id;
        event.event_date = event_date;
        event.value = value;
        event.lastUpdatedDate = lastUpdatedDate;
        event.serie = serie;

        return event;
    }
}
