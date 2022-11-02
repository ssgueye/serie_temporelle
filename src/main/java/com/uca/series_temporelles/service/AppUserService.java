package com.uca.series_temporelles.service;

import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> getAllAppUser(){
        List<AppUser> appUsers = new ArrayList<AppUser>();
        appUserRepository.findAll().forEach(appUser -> appUsers.add(appUser));
        return appUsers;
    }
    public AppUser getAppUser(Long id){
        return appUserRepository.findById(id).get();
    }
    public void deleteAppUser(Long id){
        appUserRepository.deleteById(id);
    }
    public void saveAppUser(AppUser appUser){
        appUserRepository.save(appUser);
    }

}
