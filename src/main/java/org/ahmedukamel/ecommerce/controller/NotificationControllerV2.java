package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.NotificationRequestV2;
import org.ahmedukamel.ecommerce.dto.request.NotificationUsersRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.notification.NotificationServiceV2;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v2/admin/notify")
public class NotificationControllerV2 {
    private final NotificationServiceV2 service;

    @PostMapping(value = "customers")
    public ResponseEntity<ApiResponse> notifyCustomers(@Valid @RequestBody NotificationUsersRequest request) {
        ApiResponse response = service.notifyCustomers(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "all")
    public ResponseEntity<ApiResponse> notifyAllCustomers(@Valid @RequestBody NotificationRequestV2 request) {
        ApiResponse response = service.notifyAllCustomers(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{notificationId}")
    public ResponseEntity<ApiResponse> updateNotification(@Valid @RequestBody NotificationRequestV2 request, @PathVariable(value = "notificationId") Integer notificationId) {
        ApiResponse response = service.updateNotification(request, notificationId);
        return ResponseUtils.acceptedResponse(response);
    }
}
