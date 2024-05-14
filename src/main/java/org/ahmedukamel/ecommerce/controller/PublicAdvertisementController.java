package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.advertisement.AdvertisementService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/public/advertisement")
public class PublicAdvertisementController {
    private final AdvertisementService service;

    @GetMapping(value = "{advertisementId}")
    public ResponseEntity<ApiResponse> getAdvertisement(@PathVariable(value = "advertisementId") Integer advertisementId) {
        ApiResponse response = service.getAdvertisement(advertisementId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getAllAdvertisements() {
        ApiResponse response = service.getAllAdvertisements();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "active")
    public ResponseEntity<ApiResponse> getActiveAdvertisements() {
        ApiResponse response = service.getActiveAdvertisements();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "inactive")
    public ResponseEntity<ApiResponse> getInactiveAdvertisements() {
        ApiResponse response = service.getInactiveAdvertisements();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "paused")
    public ResponseEntity<ApiResponse> getPausedAdvertisements() {
        ApiResponse response = service.getPausedAdvertisements();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "pending")
    public ResponseEntity<ApiResponse> getPendingAdvertisements() {
        ApiResponse response = service.getPendingAdvertisements();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "expired")
    public ResponseEntity<ApiResponse> getExpiredAdvertisements() {
        ApiResponse response = service.getExpiredAdvertisements();
        return ResponseUtils.acceptedResponse(response);
    }
}
