package com.example.demo.controllers;

import com.example.demo.entities.AppUser;
import com.example.demo.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable int id) {
        Optional<AppUser> appUser = appUserService.getUserById(id);
        return appUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create")
    public AppUser createUser(@RequestBody AppUser appUser) {
        return appUserService.createUser(appUser);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable int id, @RequestBody AppUser appUser) {
        appUser.setId(id);
        return ResponseEntity.ok(appUserService.updateUser(appUser));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}