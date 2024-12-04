package com.example.demo.services;

import com.example.demo.entities.AppUser;
import com.example.demo.entities.Journal;
import com.example.demo.repositories.AppUserRepository;
import com.example.demo.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public Optional<Journal> getJournalById(int id) {
        return journalRepository.findById(id);
    }

    public String createJournal(Journal journal) {
        journalRepository.save(journal);
        return "success";

    }


public Journal updateJournal(Journal journal, int id) {
    // Ensure the journal exists before updating
    Journal existingJournal = journalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Journal not found"));

    existingJournal.setTitle(journal.getTitle());
    existingJournal.setContent(journal.getContent());
    existingJournal.setStatus(journal.getStatus());
    existingJournal.setAppUser(journal.getAppUser());
    existingJournal.setImg(journal.getImg());

    return journalRepository.save(existingJournal);
}

    public void deleteJournal(int id) {
        journalRepository.deleteById(id);
    }
}
