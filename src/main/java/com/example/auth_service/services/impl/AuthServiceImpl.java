package com.example.auth_service.services.impl;

import com.example.auth_service.common.dtos.TokenResponse;
import com.example.auth_service.common.dtos.UserRequest;
import com.example.auth_service.common.entities.UserModel;
import com.example.auth_service.repositories.UserRepository;
import com.example.auth_service.services.AuthService;
import com.example.auth_service.services.JwtService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public TokenResponse createUser(UserRequest userRequest) {
        return Optional.of(userRequest)
                .map(this::mapToEntity)
                .map(userRepository::save)
                .map(userCreated -> jwtService.generateToken(userCreated.getId()))
                .orElseThrow(() -> new RuntimeException("User creation failed"));
    }

    private UserModel mapToEntity(UserRequest userRequest) {
        return UserModel.builder()
                .email(userRequest.getEmail())
                .password((userRequest.getPassword()))
                .role("USER")
                .build();
    }
}
