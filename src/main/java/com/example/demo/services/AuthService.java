package com.example.demo.services;

import com.example.demo.dto.ReqRes;
import com.example.demo.entities.AppUser;
import com.example.demo.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JavaMailSender mailSender;


    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            AppUser appUser = new AppUser();
            appUser.setEmail(registrationRequest.getEmail());
            appUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            AppUser appUserRes = appUserRepository.save(appUser);
            if (appUserRes != null && appUserRes.getId()>0) {
                resp.setAppUser(appUserRes);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes signIn(ReqRes signinRequest){
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
            var user = appUserRepository.findByEmail(signinRequest.getEmail()).orElseThrow();
            System.out.println("USER IS: "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("1Hr");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractEmail(refreshTokenReqiest.getToken());
        AppUser users = appUserRepository.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }

//    // Password Reset Request (New Method)
//    public ReqRes requestPasswordReset(String email) {
//        ReqRes response = new ReqRes();
//        try {
//            // Find the user by email
//            AppUser user = appUserRepository.findByEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//
//            // Generate password reset token
//            String resetToken = jwtUtils.generateToken(user);
//
//            // Send the reset token via email
//            sendPasswordResetEmail(email, resetToken);
//
//            response.setStatusCode(200);
//            response.setMessage("Password reset link has been sent to your email.");
//        } catch (Exception e) {
//            response.setStatusCode(500);
//            response.setError(e.getMessage());
//        }
//        return response;
//    }
//
//    // Password Reset Handling (New Method)
//    public ReqRes resetPassword(String resetToken, String newPassword) {
//        ReqRes response = new ReqRes();
//        try {
//            // Validate the reset token
//            String email = jwtUtils.extractEmail(resetToken);
//            AppUser user = appUserRepository.findByEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//
//            if (jwtUtils.isTokenExpired(resetToken)) {
//                throw new RuntimeException("Reset token has expired.");
//            }
//
//            // Update password
//            user.setPassword(passwordEncoder.encode(newPassword));
//            appUserRepository.save(user);
//
//            response.setStatusCode(200);
//            response.setMessage("Password has been reset successfully.");
//        } catch (Exception e) {
//            response.setStatusCode(500);
//            response.setError(e.getMessage());
//        }
//        return response;
//    }
//
//    // Send Password Reset Email (Helper Method)
//    public void sendPasswordResetEmail(String toEmail, String resetToken) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("Password Reset Request");
//        message.setText("To reset your password, click the link below:\n" +
//                "http://localhost:8080/reset-password?token=" + resetToken);
//        mailSender.send(message);
//    }


}
