package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.ProductDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.service.product.ProductServiceV2;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.ahmedukamel.ecommerce.validation.annotation.ValidCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ProductControllerV2 {
    private final ProductServiceV2 service;

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @PostMapping(value = "api/v2/custom/product/new")
    public ResponseEntity<ApiResponse> addProduct(@RequestParam @NotNull @NotBlank String englishName,
                                                  @RequestParam @NotNull @NotBlank String arabicName,
                                                  @RequestParam @NotNull @NotBlank String frenchName,
                                                  @RequestParam @NotNull @NotBlank String englishDescription,
                                                  @RequestParam @NotNull @NotBlank String arabicDescription,
                                                  @RequestParam @NotNull @NotBlank String frenchDescription,
                                                  @RequestParam @NotNull @NotBlank String englishAbout,
                                                  @RequestParam @NotNull @NotBlank String frenchAbout,
                                                  @RequestParam @NotNull @NotBlank String arabicAbout,
                                                  @RequestParam @ValidCategory Integer categoryId,
                                                  @RequestParam @NotNull @Min(0) Double price,
                                                  @RequestParam @NotNull @Min(0) Double weight,
                                                  @RequestParam @NotNull @Min(0) Double priceAfterDiscount,
                                                  @RequestParam @NotNull Boolean isDiscount,
                                                  @RequestParam @NotNull @Min(0) Integer stockQuantity,
                                                  @RequestParam @NotNull MultipartFile image1,
                                                  @RequestParam(required = false) MultipartFile image2,
                                                  @RequestParam(required = false) MultipartFile image3,
                                                  @RequestParam(required = false) MultipartFile image4,
                                                  @RequestParam(required = false) MultipartFile image5) throws IOException {
        if (image1.isEmpty()) {
            throw new CustomException("Image 1 is empty");
        }
        ApiResponse response = service.addProduct(englishName, arabicName, frenchName,
                englishDescription, arabicDescription, frenchDescription,
                englishAbout, frenchAbout, arabicAbout,
                categoryId, price, weight, priceAfterDiscount, isDiscount,
                stockQuantity, image1, image2,
                image3, image4, image5);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @PutMapping(value = "api/v2/custom/product/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable(value = "productId") Integer productId, @Valid @RequestBody ProductDtoV2 request) {
        ApiResponse response = service.updateProduct(request, productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "api/v2/public/product/{productId}")
    public ResponseEntity<ApiResponse> getProduct(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.getProduct(productId);
        return ResponseUtils.acceptedResponse(response);
    }
}