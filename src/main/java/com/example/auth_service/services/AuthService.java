package com.example.auth_service.services;

import com.example.auth_service.common.dtos.TokenResponse;
import com.example.auth_service.common.dtos.UserRequest;

public interface AuthService {
    TokenResponse createUser(UserRequest userRequest);

    TokenResponse loginUser(UserRequest userRequest);
}
