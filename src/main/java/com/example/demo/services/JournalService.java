package com.example.demo.services;

import com.example.demo.entities.Journal;
import com.example.demo.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public Optional<Journal> getJournalById(int id) {
        return journalRepository.findById(id);
    }

    public Journal createJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    public Journal updateJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    public void deleteJournal(int id) {
        journalRepository.deleteById(id);
    }
}
