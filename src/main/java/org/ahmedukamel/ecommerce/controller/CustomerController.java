package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.CustomerService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/user")
public class CustomerController {
    private final CustomerService service;

    @PutMapping(value = "demand/product/{productId}")
    public ResponseEntity<ApiResponse> demandProduct(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.demandProduct(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "coupons")
    public ResponseEntity<?> getCoupons() {
        ApiResponse response = service.getCoupons();
        return ResponseEntity.ok().body(response);
    }
}
