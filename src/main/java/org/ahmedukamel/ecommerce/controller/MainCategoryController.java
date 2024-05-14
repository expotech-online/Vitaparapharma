package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.MainCategoryDto;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.mainCategory.MainCategoryService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/admin/main/category")
public class MainCategoryController {
    private final MainCategoryService service;

    public MainCategoryController(@Qualifier(value = "mainCategoryServiceV1") MainCategoryService service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addMainCategory(@Valid @RequestBody MainCategoryDto request) {
        ApiResponse response = service.addMainCategory(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{categoryId}")
    public ResponseEntity<ApiResponse> updateMainCategory(@PathVariable(value = "categoryId") Integer productId, @RequestBody MainCategoryDto request) {
        ApiResponse response = service.updateMainCategory(request, productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteMainCategory(@PathVariable(value = "categoryId") Integer categoryId) {
        ApiResponse response = service.deleteMainCategory(categoryId);
        return ResponseUtils.acceptedResponse(response);
    }
}
