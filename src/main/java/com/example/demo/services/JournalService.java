package com.example.demo.services;

import com.example.demo.entities.AppUser;
import com.example.demo.entities.Journal;
import com.example.demo.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private AppUserService appUserService;

    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public Optional<Journal> getJournalById(int id) {
        return journalRepository.findById(id);
    }

    public Journal createJournal(Journal journal, String userEmail) {
        // Find the user by email
        AppUser user = appUserService.findByEmail(userEmail);

        // Assign the user to the journal
        journal.setAppUser(user);

        // Save the journal
        return journalRepository.save(journal);
    }

    public Journal updateJournal(int journalId, Journal updatedJournal, String userEmail) {
        // Find the user by email
        AppUser user = appUserService.findByEmail(userEmail);

        // Find the journal by ID
        Journal existingJournal = journalRepository.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        // Ensure the journal belongs to the user
        if (!existingJournal.getAppUser().getId().equals(user.getId())) {
            throw new RuntimeException("This journal does not belong to the current user");
        }

        // Update the journal fields
        existingJournal.setTitle(updatedJournal.getTitle());
        existingJournal.setContent(updatedJournal.getContent());
        existingJournal.setStatus(updatedJournal.getStatus());
        existingJournal.setFavorite(updatedJournal.getFavorite());

        // Save the updated journal
        return journalRepository.save(existingJournal);
    }

    public void deleteJournal(int journalId, String userEmail) {
        // Find the user by email
        AppUser user = appUserService.findByEmail(userEmail);

        // Find the journal by ID
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        // Ensure the journal belongs to the user
        if (!journal.getAppUser().getId().equals(user.getId())) {
            throw new RuntimeException("This journal does not belong to the current user");
        }

        // Delete the journal
        journalRepository.delete(journal);
    }

//    public Journal setFavorite(int productId, boolean favorite) {
//        Journal journal = journalRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        journal.setFavorite(favorite);
//        return journalRepository.save(journal);
//    }

    public List<Journal> getFavoriteJournalsByEmail(String email) {
        // Get all favorite journals related to the user with the provided email
        return journalRepository.findByAppUserEmailAndFavorite(email, true);
    }

    public String uploadImage(MultipartFile file) throws IOException {

        Journal imageData = journalRepository.save(Journal.builder()
                .title(file.getOriginalFilename())
                .content(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadImage(String fileName){
        Optional<Journal> dbImageData = journalRepository.findByTitle(fileName);
        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }
}
