package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.UserSerie;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
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

    @Autowired
    public UserSerieService(SerieRepository serieRepository,
                        UserSerieRepository userSerieRepository,
                        AppUserService appUserService,
                            SerieService serieService) {
        this.serieRepository = serieRepository;
        this.userSerieRepository = userSerieRepository;
        this.appUserService = appUserService;
        this.serieService = serieService;
    }

    public Iterable<UserSerie> getAllUserSeries(String pseudo_user){
        return StreamUtils.createStreamFromIterator(
                userSerieRepository.
                        getUserSerieEntitiesByAppUserPseudo(pseudo_user).iterator()).
                map(this::toUserSerie).collect(Collectors.toList());
    }

    public UserSerie createSerie(String pseudo_user, Serie serie){

        Assert.hasText(pseudo_user, "pseudo can not be null/empty/blank");
        Assert.notNull(serie, "Serie can not be null");

        AppUser appUser = appUserService.getOne(pseudo_user);
        //Save the Serie
        serieRepository.save(serieService.toSerieEntity(serie));

        //Create and save the UserSerie
        UserSerie userSerie = new UserSerie(Permission.WRITE_READ, true, appUser, serie);

        return toUserSerie(userSerieRepository.save(toUserSerieEntity(userSerie)));
    }

    public Serie updateSerie(Long serie_id, String pseudo_user, Serie serie){

        Assert.hasText(pseudo_user, "pseudo can not be null/empty/blank");
        Assert.notNull(serie_id, "Serie_id can not be null");

        UserSerieEntity userSerieEntity = userSerieRepository.
                getUserSerieEntitiesByAppUserPseudoAndSerieId(pseudo_user, serie_id);
        if(userSerieEntity.isOwner || userSerieEntity.permission.equals(Permission.WRITE_READ)){
            Serie updatedSerie = serieService.
                    toSerie(serieRepository.findById(serie_id).
                                    orElseThrow(()-> new ResourceNotFoundException("Serie "+serie_id+" not found")));

            updatedSerie.title = serie.title;
            updatedSerie.description = serie.description;
            updatedSerie.lastUpdatedDate = LocalDateTime.now();

            Iterable<UserSerieEntity> userSerieEntities = userSerieRepository.getUserSerieEntitiesBySerieId(serie_id);
            for(UserSerieEntity userSerie : userSerieEntities){
                userSerie.serie = serieService.toSerieEntity(updatedSerie);
                userSerieRepository.save(userSerie);
            }

            return serieService.toSerie(serieRepository.save(serieService.toSerieEntity(updatedSerie)));

        }
        else{
            throw new NoAccessDataException("User "+pseudo_user+" doesn't have the right to update Serie"+serie_id);
        }

    }

    public UserSerieEntity shareSerie(Long serie_id,
                                      String pseudoOwner,
                                      String pseudoNotOwner,
                                      UserSerieEntity userSerieEntity){

        Assert.notNull(serie_id, "Serie id can not be null");
        Assert.hasText(pseudoOwner, "pseudo can not be null/empty/blank");
        Assert.hasText(pseudoNotOwner, "pseudo can not be null/empty/blank");
        Assert.notNull(userSerieEntity, "UserSerieEntity can not be null");
        Assert.notNull(userSerieEntity.permission, "Permission is mandatory");

        UserSerieEntity userSerie = userSerieRepository.getUserSerieEntitiesByAppUserPseudoAndSerieId(pseudoOwner, serie_id);
        Assert.notNull(userSerie, "UserSerie can not be null");

        if(userSerie.isOwner){
            UserSerieEntity sharedSerie = new UserSerieEntity();
            sharedSerie.permission = userSerieEntity.permission;
            sharedSerie.isOwner = false;
            sharedSerie.serie = userSerie.serie;
            sharedSerie.appUser= appUserService.toUserEntity(appUserService.getOne(pseudoNotOwner));

            return sharedSerie;
        }
        else{
            throw new NoAccessDataException("User "+pseudoOwner+" doesn't have the right to share Serie"+serie_id);
        }

    }

    //only the owner's Serie can delete his serie
    public void deleteSerie(Long serie_id, String pseudo){
        try {
            UserSerieEntity userSerie = userSerieRepository.getUserSerieEntitiesByAppUserPseudoAndSerieId(pseudo, serie_id);
            Assert.notNull(userSerie, "UserSerie can not be null");
            if(userSerie.isOwner){
                //The execution order is important here
                //We delete the series in USER_SERIE TABLE FIRST
                Iterable<UserSerieEntity> userSerieEntities = userSerieRepository.getUserSerieEntitiesBySerieId(serie_id);
                userSerieRepository.deleteAll(userSerieEntities);
                //And after we delete the serie in SERIE TABLE
                serieRepository.deleteById(serie_id);
            }
            else {
                throw new NoAccessDataException("User "+pseudo+" doesn't have the right to share Serie"+serie_id);
            }

        }catch (EmptyResultDataAccessException e){
            //We can safely ignore this one
        }
    }

    private UserSerie toUserSerie(UserSerieEntity userSerieEntity){
        Assert.notNull(userSerieEntity, "UserSerieEntity can not be null");

        return new UserSerie(userSerieEntity.permission,
                userSerieEntity.isOwner,
                appUserService.toUser(userSerieEntity.appUser),
                serieService.toSerie(userSerieEntity.serie));
    }

    private UserSerieEntity toUserSerieEntity(UserSerie userSerie){

        UserSerieEntity userSerieEntity = new UserSerieEntity();
        userSerieEntity.permission = userSerie.permission;
        userSerieEntity.isOwner = userSerie.isOwner;
        userSerieEntity.appUser = appUserService.toUserEntity(userSerie.appUser);
        userSerieEntity.serie = serieService.toSerieEntity(userSerie.serie);

        return userSerieEntity;
    }
}
