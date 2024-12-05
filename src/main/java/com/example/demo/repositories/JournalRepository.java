package com.example.demo.repositories;

import com.example.demo.entities.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
    List<Journal> findByFavorite(boolean favorite);
    List<Journal> findByAppUserEmailAndFavorite(String email, boolean favorite);


}
