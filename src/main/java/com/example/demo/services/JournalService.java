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

    public Journal createJournal(Journal journal) {
        journal.setTitle(journal.getTitle());
        journal.setContent(journal.getContent());
        journal.setStatus(journal.getStatus());
        journal.setAppUser(journal.getAppUser());
        return journalRepository.save(journal);
    }


public Journal updateJournal(Journal journal) {
    // Ensure the journal exists before updating
    Journal existingJournal = journalRepository.findById(journal.getId())
            .orElseThrow(() -> new IllegalArgumentException("Journal not found"));

    // Update fields while retaining existing data for null or empty values
    existingJournal.setTitle(journal.getTitle() != null ? journal.getTitle() : existingJournal.getTitle());
    existingJournal.setContent(journal.getContent() != null ? journal.getContent() : existingJournal.getContent());
    existingJournal.setStatus(journal.getStatus() != null ? journal.getStatus() : existingJournal.getStatus());
    existingJournal.setImg(journal.getImg() != null ? journal.getImg() : existingJournal.getImg());

    if (journal.getAppUser() != null && journal.getAppUser().getId() > 0) {
        AppUser appUser = appUserRepository.findById(journal.getAppUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingJournal.setAppUser(appUser);
    }

    return journalRepository.save(existingJournal);
}

    public void deleteJournal(int id) {
        journalRepository.deleteById(id);
    }
}
