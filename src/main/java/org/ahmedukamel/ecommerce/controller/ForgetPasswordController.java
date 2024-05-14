package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.ChangePasswordRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailCodeRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.ForgetPasswordService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "p")
public class ForgetPasswordController {
    private final ForgetPasswordService service;

    @PostMapping(value = "forget-password")
    public ResponseEntity<ApiResponse> sendForgetPasswordCode(@Valid @RequestBody EmailRequest request) {
        ApiResponse response = service.sendForgetPasswordCode(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "validate-forget-password-code")
    public ResponseEntity<ApiResponse> validateCode(@Valid @RequestBody EmailCodeRequest request) {
        ApiResponse response = service.validateCode(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        ApiResponse response = service.changePassword(request);
        return ResponseUtils.acceptedResponse(response);
    }
}
