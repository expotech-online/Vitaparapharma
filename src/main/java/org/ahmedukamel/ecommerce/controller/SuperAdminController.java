package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.RoleRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.SuperAdminService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/super/admin")
public class SuperAdminController {
    private final SuperAdminService service;

    @PutMapping(value = "customer/role")
    public ResponseEntity<ApiResponse> setCustomerRole(@Valid @RequestBody RoleRequest request) {
        ApiResponse response = service.setCustomerRole(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "customer/disable/{customerId}")
    public ResponseEntity<ApiResponse> setCustomerDisable(@PathVariable(value = "customerId") Integer customerId) {
        ApiResponse response = service.setCustomerDisable(customerId);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "customer/enable/{customerId}")
    public ResponseEntity<ApiResponse> setCustomerEnable(@PathVariable(value = "customerId") Integer customerId) {
        ApiResponse response = service.setCustomerEnable(customerId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "role/all")
    public ResponseEntity<ApiResponse> getRoles() {
        ApiResponse response = service.getRoles();
        return ResponseUtils.acceptedResponse(response);
    }
}
