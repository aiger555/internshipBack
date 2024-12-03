package com.example.demo.services;

import com.example.demo.entities.Favorite;
import com.example.demo.repositories.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    public Optional<Favorite> getFavoriteById(int id) {
        return favoriteRepository.findById(id);
    }

    public Favorite createFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public Favorite updateFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public void deleteFavorite(int id) {
        favoriteRepository.deleteById(id);
    }
}
