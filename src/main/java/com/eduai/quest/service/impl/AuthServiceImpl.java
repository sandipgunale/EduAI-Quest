package com.eduai.quest.service.impl;

import com.eduai.quest.model.dto.auth.JwtResponse;
import com.eduai.quest.model.dto.auth.LoginRequest;
import com.eduai.quest.model.dto.auth.RegisterRequest;
import com.eduai.quest.model.entity.User;
import com.eduai.quest.repository.UserRepository;
import com.eduai.quest.service.AuthService;
import com.eduai.quest.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>())
                .enabled(true)
                .build();

        user.getRoles().add(request.getRole() != null ? request.getRole() : "STUDENT");

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser.getEmail());

        return buildJwtResponse(token, savedUser);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return buildJwtResponse(token, user);
    }

    @Override
    @Transactional
    public User processOAuth2User(String email, String name, String googleId) {
        // First try to find by Google ID
        User user = userRepository.findByGoogleId(googleId)
                .orElse(null);

        // If not found by Google ID, try by email
        if (user == null) {
            user = userRepository.findByEmail(email)
                    .orElse(null);
        }

        if (user == null) {
            // Create new user with Google OAuth
            String generatedPassword = passwordEncoder.encode(UUID.randomUUID().toString());

            user = User.builder()
                    .email(email)
                    .name(name != null ? name : email.split("@")[0])
                    .password(generatedPassword)
                    .googleId(googleId)
                    .roles(new HashSet<>())
                    .enabled(true)
                    .build();

            user.getRoles().add("STUDENT");
            user = userRepository.save(user);
            log.info("Created new OAuth2 user: {}", email);
        } else {
            // Update existing user with Google ID if not set
            if (user.getGoogleId() == null) {
                user.setGoogleId(googleId);
                user = userRepository.save(user);
                log.info("Updated existing user with Google ID: {}", email);
            }
        }

        return user;
    }

    @Override
    @Transactional
    public JwtResponse loginOrRegisterOAuthUser(String email, String name, String googleId) {
        User user = processOAuth2User(email, name, googleId);
        String token = jwtTokenProvider.generateToken(user.getEmail());

        // Set authentication context for Spring Security
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return buildJwtResponse(token, user);
    }

    private JwtResponse buildJwtResponse(String token, User user) {
        JwtResponse response = new JwtResponse();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setRoles(user.getRoles());
        return response;
    }
}