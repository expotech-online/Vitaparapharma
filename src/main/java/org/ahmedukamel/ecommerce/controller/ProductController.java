package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.ProductDto;
import org.ahmedukamel.ecommerce.service.product.ProductService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
@RequestMapping(value = "api/v1/custom/product")
public class ProductController {
    private final ProductService service;

    public ProductController(@Qualifier("productServiceImpl") ProductService service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody ProductDto request) {
        ApiResponse response = service.addProduct(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable(value = "productId") Integer productId, @RequestBody ProductDto request) {
        ApiResponse response = service.updateProduct(request, productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "picture/add/{productId}")
    public ResponseEntity<ApiResponse> uploadProductImage(@PathVariable(value = "productId") Integer productId, @ModelAttribute(value = "image") MultipartFile image) throws IOException {
        ApiResponse response = service.uploadProductImage(productId, image);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "picture/delete/{productId}/{pictureName}")
    public ResponseEntity<ApiResponse> deleteProductImage(@PathVariable(value = "productId") Integer productId, @PathVariable(value = "pictureName") String pictureName) {
        ApiResponse response = service.deleteProductImage(productId, pictureName);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "activate/{productId}")
    public ResponseEntity<ApiResponse> activateProduct(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.activateProduct(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "deactivate/{productId}")
    public ResponseEntity<ApiResponse> deactivateProduct(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.deactivateProduct(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "inactive/all")
    public ResponseEntity<ApiResponse> getInactiveProducts() {
        ApiResponse response = service.getInactiveProducts();
        return ResponseUtils.acceptedResponse(response);
    }
}