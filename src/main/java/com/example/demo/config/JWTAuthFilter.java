package com.example.demo.config;

import com.example.demo.services.AppUserService;
import com.example.demo.services.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AppUserService appUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String userEmail;

        // Get email from JWT token in the Authorization header
        userEmail = jwtUtils.getEmailFromAuthHeader(authHeader); // This method now exists in JWTUtils

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Find user by email
                UserDetails userDetails = appUserService.findByEmail(userEmail);

                // Validate token
                if (jwtUtils.isTokenValid(authHeader.substring(7), userDetails)) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Set the authentication object in the security context
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (UsernameNotFoundException ex) {
                logger.error("User not found: " + ex.getMessage());
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}

