package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.request.RegistrationRequest;
import org.ahmedukamel.ecommerce.exception.DeprecationException;
import org.ahmedukamel.ecommerce.service.AuthService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/auth")
public class AuthController {
    private final AuthService service;

    @PostMapping(value = "register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest request) {
        throw new DeprecationException();
//        ApiResponse response = service.register(request);
//        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "login")
    public ResponseEntity<ApiResponse> login(@RequestBody RegistrationRequest request) {
        throw new DeprecationException();
//        ApiResponse response = service.login(request);
//        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "oauth")
    public ResponseEntity<ApiResponse> oauth(@RequestParam(value = "token") String token) {
        ApiResponse response = new ApiResponse(true, "User have been logged in successfully.", Map.of("token", token));
        return ResponseUtils.acceptedResponse(response);
    }
}
