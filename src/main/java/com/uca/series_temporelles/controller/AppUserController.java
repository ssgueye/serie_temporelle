package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class AppUserController {

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/users")
    public List<AppUser> getAllAppUser(){
        return appUserRepository.findAll();
    }
    @GetMapping("/user/{id}")
    public AppUser getAppUser(@PathVariable("id") Long id){
        return appUserRepository.findById(id).orElseThrow(()-> new IllegalArgumentException()) ;
    }
    @DeleteMapping("/user/{id}")
    public void deleteAppUser(@PathVariable("id") Long id){
        appUserRepository.deleteById(id);
    }
    @PostMapping("/user")
    public ResponseEntity<AppUser> saveAppUser(@RequestBody AppUser appUser){
        AppUser appUserSaved = appUserRepository.save(appUser);
        String url = "/user/"+appUserSaved.getId();
        return  ResponseEntity.created(URI.create(url)).body(appUserSaved);
    }

}
