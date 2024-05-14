package org.ahmedukamel.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ahmedukamel.ecommerce.dto.AdvertisementDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.advertisement.AdvertisementServiceV2;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
@RequestMapping(value = "api/v2/custom/advertisement")
@Validated
public class AdvertisementControllerV2 {
    private final AdvertisementServiceV2 service;

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addAdvertisement(@Valid @ModelAttribute(value = "advertisement") AdvertisementDtoV2 request,
                                                        @NotNull @ModelAttribute(value = "image") MultipartFile image) throws IOException {
        ApiResponse response = service.addAdvertisement(request, image);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{advertisementId}")
    public ResponseEntity<ApiResponse> updateAdvertisement(@RequestBody AdvertisementDtoV2 request, @PathVariable(value = "advertisementId") int advertisementId) {
        ApiResponse response = service.updateAdvertisement(request, advertisementId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "{advertisementId}")
    public ResponseEntity<ApiResponse> getAdvertisementById(@PathVariable(value = "advertisementId") Integer advertisementId) {
        ApiResponse response = service.getAdvertisementById(advertisementId);
        return ResponseUtils.acceptedResponse(response);
    }

    @Component
    @RequiredArgsConstructor
    public static class StringToAdvertisementDtoV2Converter implements Converter<String, AdvertisementDtoV2> {

        final ObjectMapper objectMapper;

        @SneakyThrows
        @Override
        public AdvertisementDtoV2 convert(@NonNull String source) {
            return objectMapper.readValue(source, AdvertisementDtoV2.class);
        }
    }
}
