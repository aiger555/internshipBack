package com.example.demo.repositories;

import com.example.demo.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
}
