package com.example.auth_service.controllers;

import com.example.auth_service.common.constants.ApiPathConstants;
import com.example.auth_service.common.dtos.TokenResponse;
import com.example.auth_service.common.dtos.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE) // + ApiPathConstants.LOGIN_ROUTE)
public interface AuthApi {
    @PostMapping(value = "/register")
    ResponseEntity<TokenResponse> createUser(@Valid @RequestBody UserRequest userRequest);

    @PostMapping(value ="/login" )
    ResponseEntity<TokenResponse> loginUser(@Valid @RequestBody UserRequest userRequest);

    @GetMapping//(value = {"/{userId}"})
    ResponseEntity<String> getUser(@RequestAttribute(name="X-User-Id") String userId);

}
