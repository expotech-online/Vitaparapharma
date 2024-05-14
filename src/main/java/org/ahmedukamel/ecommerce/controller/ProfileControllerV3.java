package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.constraints.NotNull;
import org.ahmedukamel.ecommerce.dto.CustomerDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.ProfileService;
import org.ahmedukamel.ecommerce.service.profile.ProfileImageService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "api/v3/profile")
public class ProfileControllerV3 {
    private final ProfileService service;
    private final ProfileImageService imageService;

    public ProfileControllerV3(@Qualifier("profileServiceImplV3") ProfileService service,
                               ProfileImageService imageService) {
        this.service = service;
        this.imageService = imageService;
    }

    @PutMapping(value = "image")
    public ResponseEntity<ApiResponse> uploadImage(@NotNull @RequestParam(value = "image") MultipartFile image) throws IOException {
        ApiResponse response = imageService.uploadImage(image);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "image")
    public ResponseEntity<ApiResponse> deleteImage() throws IOException {
        ApiResponse response = imageService.deleteImage();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "notifications")
    public ResponseEntity<ApiResponse> getNotifications() {
        ApiResponse response = service.getNotifications();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "notifications/unread")
    public ResponseEntity<ApiResponse> getUnreadNotifications() {
        ApiResponse response = service.getUnreadNotifications();
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "read-notification/{notificationId}")
    public ResponseEntity<ApiResponse> readNotification(@PathVariable(value = "notificationId") Integer notificationId) {
        ApiResponse response = service.readNotification(notificationId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProfile() {
        ApiResponse response = service.getProfile();
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody CustomerDtoV2 request) {
        ApiResponse response = service.updateUser(request);
        return ResponseUtils.acceptedResponse(response);
    }
}