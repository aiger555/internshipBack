package com.example.demo.repositories;

import com.example.demo.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(Integer id);

}
