package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.SerieEntity;
import com.uca.series_temporelles.entity.UserSerieEntity;
import com.uca.series_temporelles.enumerations.Permission;
import com.uca.series_temporelles.exception.NoAccessDataException;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.Serie;
import com.uca.series_temporelles.model.UserSerie;
import com.uca.series_temporelles.repository.SerieRepository;
import com.uca.series_temporelles.repository.UserSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    @Autowired
    public UserSerieService(SerieRepository serieRepository,
                        UserSerieRepository userSerieRepository,
                        AppUserService appUserService){
        this.serieRepository = serieRepository;
        this.userSerieRepository = userSerieRepository;
        this.appUserService = appUserService;
    }

    public Iterable<UserSerieEntity> getAllUserSeries(String pseudo_user){

        return StreamUtils.createStreamFromIterator(
                userSerieRepository.
                        getUserSerieEntitiesByAppUserPseudo(pseudo_user).iterator()).collect(Collectors.toList());
    }

    public UserSerieEntity getOne(String pseudo, Long serie_id){

        return userSerieRepository.getUserSerieEntityByUserPseudoAndSerieId(pseudo, serie_id);
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
        if(userOwner!=null && userOwner.isOwner){
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

    public UserSerieEntity toUserSerieEntity(UserSerie userSerie){

        UserSerieEntity userSerieEntity = new UserSerieEntity();
        userSerieEntity.permission = userSerie.permission;
        userSerieEntity.isOwner = userSerie.isOwner;
        userSerieEntity.appUser = appUserService.toUserEntity(userSerie.appUser);
        userSerieEntity.serie = toSerieEntity(userSerie.serie);

        return userSerieEntity;
    }

    public SerieEntity toSerieEntity(Serie serie){

        SerieEntity serieEntity = new SerieEntity();
        serieEntity.title = serie.title;
        serieEntity.description = serie.description;
        serieEntity.lastUpdatedDate = serie.lastUpdatedDate;

        return serieEntity;
    }
}
