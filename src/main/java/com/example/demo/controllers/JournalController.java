package com.example.demo.controllers;

import com.example.demo.entities.AppUser;
import com.example.demo.entities.Journal;
import com.example.demo.repositories.JournalRepository;
import com.example.demo.services.JournalService;
import com.example.demo.services.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journals")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private JWTUtils jwtUtils; // Inject JWTUtils for JWT operations

    // Method to extract email from Authorization header
    private String getEmailFromAuthHeader(String authHeader) {
        return jwtUtils.getEmailFromAuthHeader(authHeader); // Use JWTUtils to extract the email
    }

    @GetMapping
    public List<Journal> getAllJournals() {
        return journalService.getAllJournals();
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
}
