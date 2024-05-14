package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.AddressRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.exception.DeprecationException;
import org.ahmedukamel.ecommerce.service.AddressService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/user/address")
public class AddressController {
    private final AddressService service;

    public AddressController(@Qualifier(value = "addressServiceImpl") AddressService service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addAddress(@RequestBody AddressRequest request) {
        throw new DeprecationException();
//        ApiResponse response = service.addAddress(request);
//        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "delete/{addressId}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable(value = "addressId") Integer addressId) {
        ApiResponse response = service.deleteAddress(addressId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getAddresses() {
        throw new DeprecationException();
//        ApiResponse response = service.getAddresses();
//        return ResponseUtils.acceptedResponse(response);
    }
}