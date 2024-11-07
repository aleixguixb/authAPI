package com.example.auth_service.services.impl;

import com.example.auth_service.common.dtos.TokenResponse;
import com.example.auth_service.common.dtos.UserRequest;
import com.example.auth_service.common.entities.UserModel;
import com.example.auth_service.repositories.UserRepository;
import com.example.auth_service.services.AuthService;
import com.example.auth_service.services.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenResponse createUser(UserRequest userRequest) {
        // Ciframos la password antes de almacenar el usuario en la BBDD
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Creamos el user y devolvemos el token junto al ID
        return Optional.of(userRequest)
                .map(this::mapToEntity)
                .map(userRepository::save)
                .map(userCreated -> TokenResponse.builder()
                        .accessToken(jwtService.generateToken(userCreated.getId()).getAccessToken())
                        .userId(userCreated.getId()) // ID user
                        .build()
                )
                .orElseThrow(() -> new RuntimeException("User creation failed"));
    }

    @Override
    public TokenResponse loginUser(UserRequest userRequest) {
        // Buscamos el usuario por email (en BBDD)
        UserModel user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verifica la password
        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        // Devolvemos el Token junto al Id
        return TokenResponse.builder()
            .accessToken(jwtService.generateToken(user.getId()).getAccessToken())
            .userId(user.getId())
            .build();

       /* return userRepository.findByEmail(userRequest.getEmail())
                .filter(user -> passwordEncoder.matches(userRequest.getPassword(), user.getPassword()))
                .map(user -> jwtService.generateToken(user.getId()))
                .orElseThrow(() -> new RuntimeException("Invalid Credentials"));*/
    }

    private UserModel mapToEntity(UserRequest userRequest) {
        return UserModel.builder()
                .email(userRequest.getEmail())
                .password((userRequest.getPassword()))
                .role("USER")
                .build();
    }
}
