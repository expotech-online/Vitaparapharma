package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.CustomerDto;
import org.ahmedukamel.ecommerce.dto.request.UpdatePasswordRequest;
import org.ahmedukamel.ecommerce.service.ProfileService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/profile")
public class ProfileController {
    private final ProfileService service;

    public ProfileController(@Qualifier("profileServiceImpl") ProfileService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProfile() {
        ApiResponse response = service.getProfile();
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody CustomerDto request) {
        ApiResponse response = service.updateUser(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "password/update")
    public ResponseEntity<ApiResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        ApiResponse response = service.updatePassword(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "notifications")
    public ResponseEntity<ApiResponse> getNotifications() {
        ApiResponse response = service.getNotifications();
        return ResponseUtils.acceptedResponse(response);
    }
}
