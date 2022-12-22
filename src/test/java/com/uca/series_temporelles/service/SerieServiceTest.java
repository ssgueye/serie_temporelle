package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.*;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.UserSerie;
import com.uca.series_temporelles.repository.EventRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.TagRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SerieServiceTest {

    @Mock
    SerieRepository serieRepository;
    @InjectMocks
    SerieService serieService;
    @Mock
    AppUserService service;
    @Mock
    UserSerieRepository userSerieRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    UserSerieService userSerieService;
    @Mock
    TagRepository tagRepository;

    private final static LocalDateTime date = LocalDateTime.of(2022, 12, 2, 19, 59);
    @Nested
    class TestSerieEntityConversion{
        @Test
        void canNotConvertNullSerieEntity(){
            assertThrows(IllegalArgumentException.class, ()-> serieService.toSerie(null));
        }

        @Test
        void canNotConvertSerieEntityWithoutATitle(){
            assertThrows(NullPointerException.class, ()-> serieService.toSerie(new SerieEntity()));
        }

        @Test
        void canConvertSerieEntity(){
            SerieEntity serieEntity = new SerieEntity();
            serieEntity.title = "Course";
            assertThat(serieService.toSerie(serieEntity)).isNotNull();
            assertThat(serieService.toSerie(serieEntity).title).isEqualTo("Course");
            assertThat(serieService.toSerie(serieEntity).description).isEqualTo(null);
            assertThat(serieService.toSerie(serieEntity).lastUpdatedDate).isNotNull();
        }
    }

    @Nested
    class TestForGetSeries{

        @Test
        void mustReturnAllUserExistingSeries(){
            var series = new SerieEntity[]{
                    newSerieEntity(1L, "title1", "desc1"),
                    newSerieEntity(2L, "title2", "desc2"),
                    newSerieEntity(3L, "title3", "desc3"),
            };

            when(serieRepository.getAllSeriesByPseudo(any())).thenReturn(Arrays.asList(series));

            var allSeries = serieService.getAllSeries("ssgueye");
            assertThat(allSeries).isNotNull();
            assertThat(allSeries)
                    .extracting(s -> Tuple.tuple(s.id_serie, s.title, s.description, s.lastUpdatedDate))
                    .containsExactly(
                            Tuple.tuple(1L, "title1", "desc1", date),
                            Tuple.tuple(2L, "title2", "desc2", date),
                            Tuple.tuple(3L, "title3", "desc3", date));

            verify(serieRepository).getAllSeriesByPseudo(any());
        }

        @Test
        void mustReturnAllUserExistingOwnSeries(){
            var series = new SerieEntity[]{
                    newSerieEntity(1L, "title1", "desc1"),
                    newSerieEntity(2L, "title2", "desc2"),
                    newSerieEntity(3L, "title3", "desc3"),
            };

            when(serieRepository.getAllOwnSeriesByPseudo(any())).thenReturn(Arrays.asList(series));

            var allSeries = serieService.getAllOwnSeries("ssgueye");
            assertThat(allSeries).isNotNull();
            assertThat(allSeries)
                    .extracting(s -> Tuple.tuple(s.id_serie, s.title, s.description, s.lastUpdatedDate))
                    .containsExactly(
                            Tuple.tuple(1L, "title1", "desc1", date),
                            Tuple.tuple(2L, "title2", "desc2", date),
                            Tuple.tuple(3L, "title3", "desc3", date));

            verify(serieRepository).getAllOwnSeriesByPseudo(any());
        }

        @Test
        void mustReturnAllUserExistingSharedSeries(){
            var series = new SerieEntity[]{
                    newSerieEntity(1L, "title1", "desc1"),
                    newSerieEntity(2L, "title2", "desc2"),
                    newSerieEntity(3L, "title3", "desc3"),
            };

            when(serieRepository.getAllSharedSeriesByPseudo(any())).thenReturn(Arrays.asList(series));

            var allSeries = serieService.getAllSharedSeries("ssgueye");
            assertThat(allSeries).isNotNull();
            assertThat(allSeries)
                    .extracting(s -> Tuple.tuple(s.id_serie, s.title, s.description, s.lastUpdatedDate))
                    .containsExactly(
                            Tuple.tuple(1L, "title1", "desc1", date),
                            Tuple.tuple(2L, "title2", "desc2", date),
                            Tuple.tuple(3L, "title3", "desc3", date));

            verify(serieRepository).getAllSharedSeriesByPseudo(any());
        }


    }

    @Nested
    class TestSerieEntityCreation{

        @Test
        void mustReturnException(){
            //For Serie Creation
            assertThrows(IllegalArgumentException.class, ()-> serieService.createSerie("", new Serie("title1", "desc1")));
            assertThrows(IllegalArgumentException.class, ()-> serieService.createSerie(" ", new Serie("title1", "desc1")));
            assertThrows(IllegalArgumentException.class, ()-> serieService.createSerie(null, new Serie("title1", "desc1")));
            assertThrows(IllegalArgumentException.class, ()-> serieService.createSerie("ssgueye", null));

            //For Serie Update
            assertThrows(IllegalArgumentException.class, ()-> serieService.updateSerie(1L,"", new Serie("title1", "desc1")));
            assertThrows(IllegalArgumentException.class, ()-> serieService.updateSerie(2L," ", new Serie("title1", "desc1")));
            assertThrows(IllegalArgumentException.class, ()-> serieService.updateSerie(3L,null, new Serie("title1", "desc1")));
            assertThrows(NullPointerException.class, ()-> serieService.updateSerie(4L,"ssgueye", null));
        }

        @Test
        void mustReturnSavedSerie(){
            var user = newUserEntity("ssgueye");
            var serieEntity = newSerieEntity(1L, "title", "desc");
            when(service.getOne(any())).thenReturn(user);
            when(userSerieRepository.save(any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true, serieEntity, user));
            when(service.toUser(user)).thenReturn(new AppUser("ssgueye"));
            userSerieService.toUserSerieEntity(new UserSerie(Permission.WRITE_READ, true,
                    new AppUser("ssgueye"), new Serie("title", "desc")));

            var savedSerie = serieService.createSerie("ssgueye",
                    new Serie("my serie", "my desc"));

            assertThat(savedSerie).isNotNull();
            assertThat(savedSerie.id_serie).isEqualTo(1L);
            assertThat(savedSerie.title).isEqualTo("title");
            assertThat(savedSerie.description).isEqualTo("desc");
            assertThat(savedSerie.lastUpdatedDate).isEqualTo(date);

            verify(userSerieRepository).save(any());
        }

        @Test
        void mustReturnUpdatedSerie(){
            var user = newUserEntity("ssgueye");
            var serieEntity = newSerieEntity(1L, "title", "desc");
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, false, serieEntity, user));

            when(serieRepository.findById(any())).thenReturn(Optional.of(serieEntity));
            when(serieRepository.save(any())).thenReturn(serieEntity);

            var serieUpdated = serieService.updateSerie(1L, "ssguye",
                    new Serie("my serie", "my desc"));

            assertThat(serieUpdated.id_serie).isEqualTo(1L);
            assertThat(serieUpdated.title).isEqualTo("my serie");
            assertThat(serieUpdated.description).isEqualTo("my desc");

            var captor = ArgumentCaptor.forClass(SerieEntity.class);
            verify(serieRepository).save(captor.capture());
            var serie = captor.getValue();
            assertThat(serie.id_serie).isEqualTo(1L);
            assertThat(serie.title).isEqualTo("my serie");
            assertThat(serie.description).isEqualTo("my desc");
            assertThat(serie.lastUpdatedDate).isEqualTo(serieUpdated.lastUpdatedDate);

        }

        @Test
        void mustReturnNoAccessDataWhenUpdating(){
            var user = newUserEntity("ssgueye");
            var serieEntity = newSerieEntity(1L, "title", "desc");
            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.READONLY, false, serieEntity, user));

            assertThrows(NoAccessDataException.class,
                    ()-> serieService.updateSerie(1L, "ssguye",
                    new Serie("my serie", "my desc")));

        }
    }

    @Nested
    class TestForDeletionOfSerie{

        @Test
        void mustReturnExceptionWhenSerieIdNullOrPseudoIsNullEmptyOrBlank(){
            assertThrows(IllegalArgumentException.class, ()-> serieService.deleteSerie(null, "ssgueye"));
            assertThrows(IllegalArgumentException.class, ()-> serieService.deleteSerie(1L, null));
            assertThrows(IllegalArgumentException.class, ()-> serieService.deleteSerie(1L, ""));
            assertThrows(IllegalArgumentException.class, ()-> serieService.deleteSerie(1L, " "));
        }

        @Test
        void mustDeleteExistingSerie(){
            var user = newUserEntity("ssgueye");
            var user1 = newUserEntity("dapieu");
            var user2 = newUserEntity("loiseau");

            var serieEntity = newSerieEntity(1L, "title", "desc");

            var userSerieEntities = new UserSerieEntity[]{
                    newUserSerieEntity(1L, Permission.WRITE_READ, true, serieEntity, user),
                    newUserSerieEntity(2L, Permission.WRITE_READ, false, serieEntity, user2),
                    newUserSerieEntity(3L, Permission.READONLY, false, serieEntity, user1)
            };

            var events = new EventEntity[]{
                    newEventEntity(1L, date, 3D, null, date, serieEntity),
                    newEventEntity(2L, date, 4D, null, date, serieEntity),
                    newEventEntity(3L, date, 10D, null, date, serieEntity),
            };
            EventEntity event = newEventEntity(1L, date, 3D, null, date, serieEntity);
            var tags = new TagEntity[]{
                    newTagEntity(1L, "FROID", event),
                    newTagEntity(2L, "FROID", event),
                    newTagEntity(3L, "NUAGEUX", event),
            };

            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, true, serieEntity, user));
            when(userSerieRepository.getAllUserSeriesBySerieId(any()))
                    .thenReturn(Arrays.asList(userSerieEntities));
            userSerieRepository.deleteAll(Arrays.asList(userSerieEntities));
            when(tagRepository.getTagsBySerieId(any())).thenReturn(Arrays.asList(tags));
            tagRepository.deleteAll(Arrays.asList(tags));
            when(eventRepository.getAllEventsBySerieId(1L)).thenReturn(Arrays.asList(events));
            eventRepository.deleteAll(Arrays.asList(events));
            serieRepository.deleteById(1L);

            serieService.deleteSerie(1L, "ssgueye");

        }

        @Test
        void canTDeleteAnotherUserSerie(){
            var user = newUserEntity("ssgueye");

            var serieEntity = newSerieEntity(1L, "title", "desc");

            when(userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(any(), any()))
                    .thenReturn(newUserSerieEntity(1L, Permission.WRITE_READ, false, serieEntity, user));

            assertThrows(NoAccessDataException.class, ()-> serieService.deleteSerie(1L, "ssgueye"));

        }

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

    private UserSerieEntity newUserSerieEntity(Long id, Permission permission, boolean owner, SerieEntity serie, AppUserEntity appUser){
        UserSerieEntity userSerieEntity = new UserSerieEntity();
        userSerieEntity.id_user_serie = id;
        userSerieEntity.permission = permission;
        userSerieEntity.isOwner = owner;
        userSerieEntity.serie = serie;
        userSerieEntity.appUser = appUser;

        return userSerieEntity;
    }

    private EventEntity newEventEntity(Long id, LocalDateTime eventDate,
                                       Double value, String comment,
                                       LocalDateTime lastUpdate, SerieEntity serie){
        EventEntity event = new EventEntity();
        event.id_event = id;
        event.event_date = eventDate;
        event.value = value;
        event.comment = comment;
        event.lastUpdatedDate = lastUpdate;
        event.serie = serie;

        return event;
    }

    private TagEntity newTagEntity(Long id, String label, EventEntity event){
        TagEntity tag = new TagEntity();
        tag.id = id;
        tag.label = label;
        tag.event = event;
        return tag;
    }
}
