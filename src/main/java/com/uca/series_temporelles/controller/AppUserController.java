package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.entity.AppUserEntity;
import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.service.AppUserService;

import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*")
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
        if(StringUtils.hasText(pseudo) && !StringUtils.containsWhitespace(pseudo)){
            return ResponseEntity.ok(appUserService.getOne(pseudo));
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("add")
    public ResponseEntity<AppUserEntity> create(@RequestBody AppUser appUser){
        try{
            Assert.notNull(appUser, "user can not be null");
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
