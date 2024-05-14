package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.AdvertisementDto;
import org.ahmedukamel.ecommerce.dto.request.AdStatusRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.advertisement.AdvertisementService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
@RequestMapping(value = "api/v1/custom/advertisement")
public class AdvertisementController {
    private final AdvertisementService service;

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addAdvertisement(@Valid @RequestBody AdvertisementDto request) {
        ApiResponse response = service.addAdvertisement(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{advertisementId}")
    public ResponseEntity<ApiResponse> updateAdvertisement(@PathVariable(value = "advertisementId") Integer advertisementId, @RequestBody AdvertisementDto request) {
        ApiResponse response = service.updateAdvertisement(advertisementId, request);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "delete/{advertisementId}")
    public ResponseEntity<ApiResponse> deleteAdvertisement(@PathVariable(value = "advertisementId") Integer advertisementId) {
        ApiResponse response = service.deleteAdvertisement(advertisementId);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "picture/add/{advertisementId}")
    public ResponseEntity<ApiResponse> uploadAdvertisementPicture(@PathVariable(value = "advertisementId") Integer advertisementId, @ModelAttribute(value = "image") MultipartFile image) throws IOException {
        ApiResponse response = service.uploadAdvertisementPicture(advertisementId, image);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "picture/delete/{advertisementId}")
    public ResponseEntity<ApiResponse> deleteAdvertisementPicture(@PathVariable(value = "advertisementId") Integer advertisementId) {
        ApiResponse response = service.deleteAdvertisementPicture(advertisementId);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping(value = "status")
    public ResponseEntity<ApiResponse> setAdvertisementStatus(@Valid @RequestBody AdStatusRequest request) {
        ApiResponse response = service.setAdvertisementStatus(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping(value = "status/all")
    public ResponseEntity<ApiResponse> getAdvertisementStatuses() {
        ApiResponse response = service.getAdvertisementStatuses();
        return ResponseUtils.acceptedResponse(response);
    }
}
