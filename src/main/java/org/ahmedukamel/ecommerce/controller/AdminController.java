package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.NotificationRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.AdminService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/admin")
public class AdminController {
    private final AdminService service;

    @DeleteMapping(value = "review/delete/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable(value = "reviewId") Integer reviewId) {
        ApiResponse response = service.deleteReview(reviewId);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "demand/reset/product/{productId}")
    public ResponseEntity<ApiResponse> resetDemands(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.resetDemands(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "demand/notify/product/{productId}")
    public ResponseEntity<ApiResponse> resetDemandsAndNotify(@PathVariable(value = "productId") Integer productId, @RequestBody NotificationRequest request) {
        ApiResponse response = service.resetDemandsAndNotify(productId, request.getMessage());
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "customer/all")
    public ResponseEntity<ApiResponse> getAllCustomers() {
        ApiResponse response = service.getAllCustomers();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "customer/{customerId}")
    public ResponseEntity<ApiResponse> getCustomer(@PathVariable(value = "customerId") Integer customerId) {
        ApiResponse response = service.getCustomer(customerId);
        return ResponseUtils.acceptedResponse(response);
    }
}
