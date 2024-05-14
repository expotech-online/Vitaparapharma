package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.PublicService;
import org.ahmedukamel.ecommerce.service.profile.ProfileImageService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/public")
public class PublicController {
    private final PublicService service;
    private final ProfileImageService imageService;

    @GetMapping(value = "category/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        ApiResponse response = service.getAllCategories();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "main/category/all")
    public ResponseEntity<ApiResponse> getAllMainCategories() {
        ApiResponse response = service.getAllMainCategories();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "main-sub/category/all")
    public ResponseEntity<ApiResponse> getAllMainAndSubCategories() {
        ApiResponse response = service.getAllMainAndSubCategories();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "main/category/{mainCategoryId}")
    public ResponseEntity<ApiResponse> getMainCategoryCategories(@PathVariable(value = "mainCategoryId") Integer mainCategoryId) {
        ApiResponse response = service.getMainCategoryCategories(mainCategoryId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "main-category/all-lang/{mainCategoryId}")
    public ResponseEntity<ApiResponse> getMainCategoryById(@PathVariable(value = "mainCategoryId") Integer mainCategoryId) {
        ApiResponse response = service.getMainCategoryById(mainCategoryId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "category/all-lang/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable(value = "categoryId") Integer categoryId) {
        ApiResponse response = service.getCategoryById(categoryId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "country/all")
    public ResponseEntity<ApiResponse> getAllCountries() {
        ApiResponse response = service.getAllCountries();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "view/{filePath}")
    public ResponseEntity<byte[]> viewProductImage(@PathVariable(value = "filePath") String filePath) throws IOException {
        return ResponseEntity.accepted().contentType(MediaType.IMAGE_PNG).body(service.viewPicture(filePath));
    }

    @GetMapping(value = "profile/{imageName}")
    public ResponseEntity<byte[]> getP(@PathVariable(value = "imageName") String imageName) throws IOException {
        return ResponseEntity.accepted().contentType(MediaType.IMAGE_PNG).body(imageService.getImage(imageName));
    }
}