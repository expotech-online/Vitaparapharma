package org.ahmedukamel.ecommerce.controller;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.product.ProductService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
@RequestMapping(value = "api/v3/custom/product")
public class ProductControllerV3 {
    private final ProductService service;

    public ProductControllerV3(@Qualifier("productServiceImplV3") ProductService service) {
        this.service = service;
    }

    @GetMapping(value = "inactive/all")
    public ResponseEntity<ApiResponse> getInactiveProducts() {
        ApiResponse response = service.getInactiveProducts();
        return ResponseUtils.acceptedResponse(response);
    }
}