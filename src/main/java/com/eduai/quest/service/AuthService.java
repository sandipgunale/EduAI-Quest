package com.eduai.quest.service;

import com.eduai.quest.model.dto.auth.JwtResponse;
import com.eduai.quest.model.dto.auth.LoginRequest;
import com.eduai.quest.model.dto.auth.RegisterRequest;
import com.eduai.quest.model.entity.User;

public interface AuthService {
    JwtResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
    User processOAuth2User(String email, String name, String googleId);

    JwtResponse loginOrRegisterOAuthUser(String email, String name, String googleId);
}