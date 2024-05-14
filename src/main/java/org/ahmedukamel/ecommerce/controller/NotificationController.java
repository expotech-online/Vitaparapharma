package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.NotificationRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.notification.NotificationService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/admin/notify")
public class NotificationController {
    private final NotificationService service;

    @PostMapping(value = "customers")
    public ResponseEntity<ApiResponse> notifyCustomers(@Valid @RequestBody NotificationRequest request) {
        ApiResponse response = service.notifyCustomers(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "all")
    public ResponseEntity<ApiResponse> notifyAllCustomers(@RequestBody NotificationRequest request) {
        ApiResponse response = service.notifyAllCustomers(request.getMessage());
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{notificationId}")
    public ResponseEntity<ApiResponse> updateNotification(@RequestBody NotificationRequest request, @PathVariable(value = "notificationId") Integer notificationId) {
        ApiResponse response = service.updateNotification(notificationId, request.getMessage());
        return ResponseUtils.acceptedResponse(response);
    }
}
