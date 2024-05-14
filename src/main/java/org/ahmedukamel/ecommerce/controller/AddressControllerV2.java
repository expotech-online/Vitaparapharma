package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import org.ahmedukamel.ecommerce.dto.request.AddressRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.AddressService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v2/user/address")
public class AddressControllerV2 {
    private final AddressService service;

    public AddressControllerV2(@Qualifier(value = "addressServiceImplV2") AddressService service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addAddress(@Valid @RequestBody AddressRequest request) {
        ApiResponse response = service.addAddress(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getAddresses() {
        ApiResponse response = service.getAddresses();
        return ResponseUtils.acceptedResponse(response);
    }
}