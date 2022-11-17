package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.entity.EventEntity;
import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.DuplicationException;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.UserSerie;
import com.uca.series_temporelles.repository.EventRepository;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/*
 * In this class, a User can:
 * create A Serie (the Serie will be saved in SERIE TABLE and in USER_SERIE TABLE).
 * list all Series he has created
 * update a Serie he has created or he has been shared
 * delete a Serie he has created
 * share a Serie with another user and specify the permissions
 * */

@Service
public class UserSerieService {

    private final SerieRepository serieRepository;
    private final UserSerieRepository userSerieRepository;
    private final AppUserService appUserService;
    private final SerieService serieService;
    private final EventRepository eventRepository;

    @Autowired
    public UserSerieService(SerieRepository serieRepository,
                        UserSerieRepository userSerieRepository,
                        AppUserService appUserService,
                            SerieService serieService,
                            EventRepository eventRepository) {
        this.serieRepository = serieRepository;
        this.userSerieRepository = userSerieRepository;
        this.appUserService = appUserService;
        this.serieService = serieService;
        this.eventRepository = eventRepository;
    }

    public Iterable<UserSerieEntity> getAllUserSeries(String pseudo_user){

        return StreamUtils.createStreamFromIterator(
                userSerieRepository.
                        getUserSerieEntitiesByAppUserPseudo(pseudo_user).iterator()).collect(Collectors.toList());
    }

    public UserSerieEntity getOne(String pseudo, Long serie_id){

        UserSerieEntity userSerie = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);

        return userSerie;
    }


    public UserSerieEntity createSerie(String pseudo_user, Serie serie){

        Assert.hasText(pseudo_user, "pseudo can not be null/empty/blank");
        Assert.notNull(serie, "Serie can not be null");

        AppUserEntity appUser = appUserService.getOne(pseudo_user);

        //Create and save the UserSerie
        //The serie will be saved also
        UserSerie userSerie = new UserSerie(Permission.WRITE_READ, true, appUserService.toUser(appUser), serie);

        return userSerieRepository.save(toUserSerieEntity(userSerie));

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

    public UserSerieEntity shareSerie(Long serie_id,
                                      String pseudoOwner,
                                      String pseudoNotOwner,
                                      Permission permission){

        Assert.notNull(serie_id, "Serie id can not be null");
        Assert.hasText(pseudoOwner, "pseudo can not be null/empty/blank");
        Assert.hasText(pseudoNotOwner, "pseudo can not be null/empty/blank");
        Assert.notNull(permission, "Permission is mandatory");

        UserSerieEntity userOwner = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudoOwner, serie_id);
        Assert.notNull(userOwner, "UserSerie can not be null");
        System.out.println(userOwner);
        if(userOwner.isOwner){
            //avoid duplications
            UserSerieEntity existingUserSerie = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudoNotOwner, serie_id);
            //If the user shares the serie to the user for the first time
            if(!pseudoNotOwner.equals(pseudoOwner) && existingUserSerie == null){
                UserSerieEntity sharedSerie = new UserSerieEntity();
                sharedSerie.permission = permission;
                sharedSerie.isOwner = false;
                sharedSerie.serie = serieRepository.findById(serie_id).orElseThrow(()->new ResourceNotFoundException("Serie "+serie_id+" is not found"));
                sharedSerie.appUser= appUserService.getOne(pseudoNotOwner);

                return userSerieRepository.save(sharedSerie);
            }
            //If the user shares the same serie to the same user, we are going to update the permission
            else if(existingUserSerie!=null && !pseudoNotOwner.equals(pseudoOwner)){
                existingUserSerie.permission = permission;
                return userSerieRepository.save(existingUserSerie);
            }
            //If the owner shares the serie to himself, we are going to return the owner
            else{
                return userOwner;
            }

        }
        else{
            throw new NoAccessDataException("User "+pseudoOwner+" doesn't have the right to share Serie "+serie_id);
        }

    }

    //only the owner's Serie can delete his serie
    public void deleteSerie(Long serie_id, String pseudo){
        try {
            UserSerieEntity userSerie = userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
            Assert.notNull(userSerie, "UserSerie can not be null");
            if(userSerie.isOwner){

                //Deletion order is important HERE

                Iterable<UserSerieEntity> userSerieEntities = userSerieRepository.getAllUserSeriesBySerieId(serie_id);
                userSerieRepository.deleteAll(userSerieEntities);

                Iterable<EventEntity> eventsToDelete = eventRepository.getAllEventsBySerieId(serie_id);
                eventRepository.deleteAll(eventsToDelete);

                serieRepository.deleteById(serie_id);
            }
            else {
                throw new NoAccessDataException("User "+pseudo+" doesn't have the right to share Serie"+serie_id);
            }

        }catch (EmptyResultDataAccessException e){
            //We can safely ignore this one
        }
    }

    public UserSerieEntity toUserSerieEntity(UserSerie userSerie){

        UserSerieEntity userSerieEntity = new UserSerieEntity();
        userSerieEntity.permission = userSerie.permission;
        userSerieEntity.isOwner = userSerie.isOwner;
        userSerieEntity.appUser = appUserService.toUserEntity(userSerie.appUser);
        userSerieEntity.serie = serieService.toSerieEntity(userSerie.serie);

        return userSerieEntity;
    }
}
