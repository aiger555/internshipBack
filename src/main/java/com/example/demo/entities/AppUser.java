package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String password;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Journal> journals;  // posts

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites;  // favorites

    @Override
    public String getUsername() {
        return email;  // Return email as username
    }

    @Override
    public String getPassword() {
        return password;  // Return password
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Add authorities for the user, you can customize this based on roles
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // You can modify based on your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // You can modify based on your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // You can modify based on your requirements
    }

    @Override
    public boolean isEnabled() {
        return true;  // You can modify based on your requirements
    }
}
