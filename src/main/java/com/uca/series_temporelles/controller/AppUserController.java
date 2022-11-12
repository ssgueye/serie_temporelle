package com.uca.series_temporelles.controller;

import com.uca.series_temporelles.model.AppUser;
import com.uca.series_temporelles.service.AppUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.CacheControl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;

@RestController
@RequestMapping("api/users")
    public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<AppUser> create(@RequestBody AppUser appUser){
        return ResponseEntity.ok(appUserService.save(appUser));
    }
    @GetMapping("{pseudo}")
    public ResponseEntity<AppUser> getOne(@PathVariable String pseudo){

            return ResponseEntity.ok().eTag(pseudo).cacheControl(CacheControl.maxAge(Duration.ofMinutes(1))).body(appUserService.getOne(pseudo));
    }
}
