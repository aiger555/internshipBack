package com.example.demo.dto;

import com.example.demo.entities.AppUser;
import com.example.demo.entities.Favorite;
import com.example.demo.entities.Journal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String username;
    private String email;
    private String password;
    private List<Journal> journals;
    private List<Favorite> favorites;
    private AppUser appUser;
}