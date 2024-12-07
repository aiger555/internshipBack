package com.example.demo.services;

import com.example.demo.entities.AppUser;
import com.example.demo.repositories.AppUserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.util.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> getUserById(int id) {
        return appUserRepository.findById(id);
    }

    public AppUser createUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public AppUser updateUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public void deleteUser(int id) {
        appUserRepository.deleteById(id);
    }

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by email (email is treated as the "username")
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(username);

        // If user is not present, throw UsernameNotFoundException
        AppUser appUser = optionalAppUser.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Convert AppUser to UserDetails (return an instance of User with username, password, and roles)
        return User.builder()
                .username(appUser.getEmail()) // Using email as the "username"
                .password(appUser.getPassword())
                .authorities("ROLE_USER") // You can adjust this to handle roles properly
                .build();
    }


    public String forgotPassword(String email) {
        Optional<AppUser> appUser = Optional.ofNullable(appUserRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found with this email: " + email)));
        try{
            emailUtil.sendSetPasswordEmail(email);
        } catch (MessagingException e){
            throw new RuntimeException("Unable to send set password email, try again!");
        }

        return "Please check your email to set new password";
    }


    public String setPassword(String email, String newPassword) {
        AppUser appUser = appUserRepository.findByEmail(email)
                          .orElseThrow(
                () -> new RuntimeException("User not found with this email: "+ email));
        appUser.setPassword(newPassword);
        appUserRepository.save(appUser);
        return "New password set successfully, login with new password";
    }
    public String verifyAccount(String email, String otp) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
            appUserRepository.save(user);
            return "OTP verified you can login";
    }

    public String regenerateOtp(String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        appUserRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }



}
