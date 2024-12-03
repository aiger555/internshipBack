package com.example.demo.services;

import com.example.demo.entities.AppUser;
import com.example.demo.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> getUserById(int id) {
        return appUserRepository.findById(id);
    }

    public AppUser createUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public AppUser updateUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public void deleteUser(int id) {
        appUserRepository.deleteById(id);
    }
}

