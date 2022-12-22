package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.*;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.UserSerie;
import com.uca.series_temporelles.repository.EventRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.TagRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SerieService {

    private final AppUserService appUserService;
    private final UserSerieRepository userSerieRepository;
    private final UserSerieService userSerieService;
    private final SerieRepository serieRepository;
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    @Autowired
    private EntityManager entityManager;

    public SerieService(AppUserService appUserService, UserSerieRepository userSerieRepository,
                        UserSerieService userSerieService, SerieRepository serieRepository,
                        EventRepository eventRepository, TagRepository tagRepository) {
        this.appUserService = appUserService;
        this.userSerieRepository = userSerieRepository;
        this.userSerieService = userSerieService;
        this.serieRepository = serieRepository;
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
    }



    public Iterable<SerieEntity> getAllSeries(String pseudo_user){

        return StreamUtils.createStreamFromIterator(
                serieRepository.getAllSeriesByPseudo(pseudo_user).iterator()).collect(Collectors.toList()
        );
    }

    public Iterable<SerieEntity> getAllOwnSeries(String pseudo_user){

        return StreamUtils.createStreamFromIterator(
                serieRepository.getAllOwnSeriesByPseudo(pseudo_user).iterator()).collect(Collectors.toList()
        );

    }

    public Iterable<SerieEntity> getAllSharedSeries(String pseudo_user){

        return StreamUtils.createStreamFromIterator(
                serieRepository.getAllSharedSeriesByPseudo(pseudo_user).iterator()).collect(Collectors.toList()
        );

    }

    public SerieEntity createSerie(String pseudo_user, Serie serie){

        Assert.hasText(pseudo_user, "pseudo can not be null/empty/blank");
        Assert.notNull(serie, "Serie can not be null");

        AppUserEntity appUser = appUserService.getOne(pseudo_user);

        //Create and save the UserSerie
        //The serie will be saved also
        UserSerie userSerie = new UserSerie(Permission.WRITE_READ, true, appUserService.toUser(appUser), serie);

        UserSerieEntity saved = userSerieRepository.save(userSerieService.toUserSerieEntity(userSerie));
        return saved.serie;

    }

    public SerieEntity updateSerie(Long serie_id, String pseudo_user, Serie serie){

        Assert.hasText(pseudo_user, "pseudo can not be null/empty/blank");
        Assert.notNull(serie_id, "Serie_id can not be null");

        UserSerieEntity userSerieEntity = userSerieRepository.
                getUserSerieEntityByUserPseudoAndSerieId(pseudo_user, serie_id);
        if(userSerieEntity.isOwner || userSerieEntity.permission.equals(Permission.WRITE_READ)){
            SerieEntity updatedSerie = serieRepository.findById(serie_id).
                    orElseThrow(()-> new ResourceNotFoundException("Serie "+serie_id+" not found"));

            updatedSerie.title = serie.title;
            updatedSerie.description = serie.description;
            updatedSerie.lastUpdatedDate = LocalDateTime.now();

            return serieRepository.save(updatedSerie);

        }
        else{
            throw new NoAccessDataException("User "+pseudo_user+" doesn't have the right to update Serie"+serie_id);
        }

    }

    //only the owner's Serie can delete his serie
    public void deleteSerie(Long serie_id, String pseudo){
        try {
            Assert.notNull(serie_id, "serieID can not be null");
            Assert.hasText(pseudo, "pseudo can not be null/empty/blank");
            UserSerieEntity userSerie = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
            Assert.notNull(userSerie, "UserSerie can not be null");
            if(userSerie.isOwner){
                //L'ORDRE DE SUPPRESSION EST IMPORTANTE
                Iterable<UserSerieEntity> userSerieEntities = userSerieRepository.getAllUserSeriesBySerieId(serie_id);
                userSerieRepository.deleteAll(userSerieEntities);
                Iterable<TagEntity> tags = tagRepository.getTagsBySerieId(serie_id);
                tagRepository.deleteAll(tags);
                Iterable<EventEntity> eventsToDelete = eventRepository.getAllEventsBySerieId(serie_id);
                eventRepository.deleteAll(eventsToDelete);
                serieRepository.deleteById(serie_id);
            }
            else {
                throw new NoAccessDataException("Only the owner can delete the Serie"+serie_id);
            }

        }catch (EmptyResultDataAccessException e){
            //We can safely ignore this one
        }
    }

    public Serie toSerie(SerieEntity serie){
        Assert.notNull(serie, "Serie can not be null");
        return new Serie(serie.title, serie.description);
    }


}
