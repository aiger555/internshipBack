package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String email;
    private String password;
    private String img;
    private LocalDateTime created;
    private LocalDateTime updated;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Journal> journals;  // posts

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites;
}
