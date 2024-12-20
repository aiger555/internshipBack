package com.example.demo.controllers;

import com.example.demo.entities.AppUser;
import com.example.demo.entities.Journal;
import com.example.demo.repositories.JournalRepository;
import com.example.demo.services.AppUserService;
import com.example.demo.services.JournalService;
import com.example.demo.services.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journals")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private JWTUtils jwtUtils; // Inject JWTUtils for JWT operations

    // Method to extract email from Authorization header
    private String getEmailFromAuthHeader(String authHeader) {
        return jwtUtils.getEmailFromAuthHeader(authHeader); // Use JWTUtils to extract the email
    }

    @GetMapping("/all")
    public List<Journal> getAllJournals() {
        return journalService.getAllJournals();
    }

    @GetMapping()
    public List<Journal> getJournals(@RequestParam String userEmail) {
        return journalService.getJournalsByUserEmail(userEmail); // Passing the user ID
    }


    @GetMapping("/{id}")
    public ResponseEntity<Journal> getJournalById(@PathVariable int id) {
        Optional<Journal> journal = journalService.getJournalById(id);
        return journal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Journal createJournal(@RequestBody Journal journal, @RequestHeader("Authorization") String authHeader) {
        String userEmail = getEmailFromAuthHeader(authHeader); // Get email from the auth header
        return journalService.createJournal(journal, userEmail); // Pass email to service
    }

    @PutMapping("update/{id}")
    public Journal updateJournal(@PathVariable int id, @RequestBody Journal journal, @RequestHeader("Authorization") String authHeader) {
        String userEmail = getEmailFromAuthHeader(authHeader); // Get email from the auth header
        return journalService.updateJournal(id, journal, userEmail); // Pass email to service
    }

    @DeleteMapping("delete/{id}")
    public void deleteJournal(@PathVariable int id, @RequestHeader("Authorization") String authHeader) {
        String userEmail = getEmailFromAuthHeader(authHeader); // Get email from the auth header
        journalService.deleteJournal(id, userEmail); // Pass email to service
    }

//    @PatchMapping("/{journalId}/favorite")
//    public Journal setFavorite(@PathVariable int journalId, @RequestParam boolean favorite) {
//        Journal journal = journalRepository.findById(journalId)
//                .orElseThrow(() -> new RuntimeException("Journal not found"));
//        journal.setFavorite(favorite);
//        return journalRepository.save(journal);
//    }

    @GetMapping("/favorites")
    public List<Journal> getFavoriteJournals() {
        // Get the email of the currently authenticated user from JWT
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getUsername();

        // Call service method to get favorite journals for the authenticated user
        return journalService.getFavoriteJournalsByEmail(email);
    }

    // Upload an image for a journal
    @PostMapping("/{journalId}/upload")
    public ResponseEntity<String> uploadImage(@PathVariable int journalId, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {
        try {
            String userEmail = getEmailFromAuthHeader(authHeader);
            Optional<Journal> journalOpt = journalService.getJournalById(journalId);

            if (journalOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal not found.");
            }

            Journal journal = journalOpt.get();
            if (!journal.getAppUser().getEmail().equals(userEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to modify this journal.");
            }

            String message = journalService.uploadImage(file, journalId);
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed: " + e.getMessage());
        }
    }


    // Download an image by file name
    @GetMapping("/download/image/{fileName}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String fileName) {
        try {
            // Load the image as a classpath resource
            Resource resource = new ClassPathResource("static/" + fileName);

            // Check if the file exists
            if (!resource.exists()) {
                throw new FileNotFoundException("File not found: " + fileName);
            }

            // Read the file bytes
            byte[] image = Files.readAllBytes(resource.getFile().toPath());

            // Return the image
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(image);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{journalId}/image/delete")
    public ResponseEntity<String> deleteImage(@PathVariable int journalId, @RequestHeader("Authorization") String authHeader) {
        String userEmail = getEmailFromAuthHeader(authHeader);
        Optional<Journal> journalOpt = journalService.getJournalById(journalId);

        if (journalOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal not found.");
        }

        Journal journal = journalOpt.get();
        if (!journal.getAppUser().getEmail().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to modify this journal.");
        }

        boolean isDeleted = journalService.deleteImage(journal.getImageName());
        return isDeleted ? ResponseEntity.ok("Image deleted successfully.") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image deletion failed.");
    }






}
