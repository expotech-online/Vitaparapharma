package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.EmailCodeRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.AccountActivationService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "p")
public class AccountActivationController {
    private final AccountActivationService service;

    @PostMapping(value = "resend-activation-code")
    public ResponseEntity<ApiResponse> resendActivationCode(@Valid @RequestBody EmailRequest request) {
        ApiResponse response = service.resendActivationCode(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "activate-account")
    public ResponseEntity<ApiResponse> activateAccount(@Valid @RequestBody EmailCodeRequest request) {
        ApiResponse response = service.activateAccount(request);
        return ResponseUtils.acceptedResponse(response);
    }
}