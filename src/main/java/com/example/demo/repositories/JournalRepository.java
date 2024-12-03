package com.example.demo.repositories;

import com.example.demo.entities.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
}
