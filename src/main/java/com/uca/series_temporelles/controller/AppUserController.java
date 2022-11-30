package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.service.AppUserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/users")
    public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<Iterable<AppUserEntity>> getAllUsers(){
        return ResponseEntity.ok(appUserService.getAllUsers());
    }

    @GetMapping("{pseudo}")
    public ResponseEntity<AppUserEntity> getOne(@PathVariable String pseudo){

            return ResponseEntity.ok().body(appUserService.getOne(pseudo));
    }

    @PostMapping("add")
    public ResponseEntity<AppUserEntity> create(@RequestBody AppUser appUser){
        try{
            AppUserEntity savedUser = appUserService.save(appUser);
            String uri = "api/users/"+savedUser.pseudo;

            return ResponseEntity
                    .created(URI.create(uri))
                    .body(savedUser);

        }catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().build();
        }

    }
}
