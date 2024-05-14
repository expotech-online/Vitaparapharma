package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.CountryService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v2/public")
public class CountryController {
    final CountryService service;

    @GetMapping(value = "country-city-region")
    public ResponseEntity<ApiResponse> getAllCountriesCitiesAndRegions() {
        ApiResponse response = service.getAllCountriesCitiesAndRegions();
        return ResponseUtils.acceptedResponse(response);
    }
}
