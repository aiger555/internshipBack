package com.example.demo.controllers;

import com.example.demo.entities.Journal;
import com.example.demo.services.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @GetMapping
    public List<Journal> getAllJournals() {
        return journalService.getAllJournals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Journal> getJournalById(@PathVariable int id) {
        Optional<Journal> journal = journalService.getJournalById(id);
        return journal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create")
    public String createJournal(@RequestBody Journal journal) {
        return journalService.createJournal(journal);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updateJournal(@PathVariable int id, @RequestBody Journal journal) {
        journal.setId(id);
        String res = String.valueOf(journalService.updateJournal(journal, id));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteJournal(@PathVariable int id) {
        journalService.deleteJournal(id);
        return ResponseEntity.noContent().build();
    }
}
