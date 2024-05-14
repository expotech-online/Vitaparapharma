package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import org.ahmedukamel.ecommerce.dto.MainCategoryDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.mainCategory.MainCategoryService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v2/admin/main/category")
public class MainCategoryControllerV2 {
    private final MainCategoryService service;

    public MainCategoryControllerV2(@Qualifier(value = "mainCategoryServiceV2") MainCategoryService service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addMainCategory(@Valid @RequestBody MainCategoryDtoV2 request) {
        ApiResponse response = service.addMainCategory(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{categoryId}")
    public ResponseEntity<ApiResponse> updateMainCategory(@Valid @RequestBody MainCategoryDtoV2 request, @PathVariable(value = "categoryId") Integer productId) {
        ApiResponse response = service.updateMainCategory(request, productId);
        return ResponseUtils.acceptedResponse(response);
    }
}
