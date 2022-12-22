package com.uca.series_temporelles.service;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.exception.ResourceNotFoundException;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

/*
* In this class, We will not allow to delete a user for security reasons
* Deleting user can affect the series he created. So we suppose in this version
* that we can not delete them.
* Also, A User can not change his pseudo because the pseudo is the primary key
* */
@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public Iterable<AppUserEntity> getAllUsers(){

        return StreamUtils.createStreamFromIterator(appUserRepository.getAll().iterator()).collect(Collectors.toList());
    }

    public AppUserEntity getOne(String pseudo){
        Assert.hasText(pseudo, "pseudo can not be null/empty/blank");
        return appUserRepository.findById(pseudo).
                orElseThrow(()-> new ResourceNotFoundException("User "+pseudo+" can not be found"));
    }

    public AppUserEntity save(AppUser appUser){
        Assert.notNull(appUser, "AppUser should not be null");
        return appUserRepository.save(toUserEntity(appUser));
    }


    public AppUser toUser(AppUserEntity userEntity){
        Assert.notNull(userEntity, "UserEntity should not be null");
        return new AppUser(userEntity.pseudo);
    }

    public AppUserEntity toUserEntity(AppUser appUser){
        AppUserEntity appUserEntity = new AppUserEntity();
        appUserEntity.pseudo = appUser.pseudo;

        return appUserEntity;
    }


}
