package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String content;
    private String status;
    private String img;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites;
}
