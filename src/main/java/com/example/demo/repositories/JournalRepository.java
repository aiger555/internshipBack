package com.example.demo.repositories;

import com.example.demo.entities.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
//    List<Journal> findByFavorite(boolean favorite);
    List<Journal> findByAppUserEmailAndFavorite(String email, boolean favorite);
    Optional<Journal> findByTitle(String title);

}
