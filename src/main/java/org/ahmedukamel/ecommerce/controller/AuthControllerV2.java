package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.LoginRequest;
import org.ahmedukamel.ecommerce.dto.request.RegistrationRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.AuthService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v2/auth")
public class AuthControllerV2 {
    private final AuthService service;

    @PostMapping(value = "register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegistrationRequest request) {
        ApiResponse response = service.register(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse response = service.login(request);
        return ResponseUtils.acceptedResponse(response);
    }
}
