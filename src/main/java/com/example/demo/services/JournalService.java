package com.example.demo.services;

import com.example.demo.entities.AppUser;
import com.example.demo.entities.Journal;
import com.example.demo.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public List<Journal> getJournalsByUserEmail(String userEmail) {
        // Find the user by email
        AppUser user = appUserService.findByEmail(userEmail);

        if (user == null) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        return journalRepository.findByAppUser(user); // Assuming you have this method in your repository
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

    public String uploadImage(MultipartFile file, int journalId) throws IOException {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        String fileName = file.getOriginalFilename();
        byte[] compressedImage = ImageUtils.compressImage(file.getBytes());

        journal.setImageName(fileName);
        journal.setImageData(compressedImage);
        journalRepository.save(journal);

        return "Image uploaded successfully: " + fileName;
    }


    public byte[] downloadImage(String fileName) throws IOException {
        Path imagePath = Paths.get(System.getProperty("user.dir"), "image", fileName);
        System.out.println("Looking for file at: " + imagePath.toAbsolutePath());
        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return Files.readAllBytes(imagePath);
    }

    public boolean deleteImage(String fileName) {
        Optional<Journal> journalOpt = journalRepository.findByImageName(fileName);

        if (journalOpt.isPresent()) {
            Journal journal = journalOpt.get();
            journal.setImageName(null);
            journal.setImageData(null);
            journalRepository.save(journal);

            Path filePath = Paths.get("src/main/resources/static", fileName);
            try {
                Files.deleteIfExists(filePath);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("Error deleting image file: " + e.getMessage());
            }
        }
        return false;
    }



}
