package com.example.demo.controllers;

import com.example.demo.entities.Favorite;
import com.example.demo.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping
    public List<Favorite> getAllFavorites() {
        return favoriteService.getAllFavorites();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Favorite> getFavoriteById(@PathVariable int id) {
        Optional<Favorite> favorite = favoriteService.getFavoriteById(id);
        return favorite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create")
    public Favorite createFavorite(@RequestBody Favorite favorite) {
        return favoriteService.createFavorite(favorite);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Favorite> updateFavorite(@PathVariable int id, @RequestBody Favorite favorite) {
        favorite.setId(id);
        return ResponseEntity.ok(favoriteService.updateFavorite(favorite));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable int id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
