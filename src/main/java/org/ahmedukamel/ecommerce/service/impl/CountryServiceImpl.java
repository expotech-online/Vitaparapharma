package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.repository.CountryRepository;
import org.ahmedukamel.ecommerce.service.CountryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CountryServiceImpl implements CountryService {
    final CountryRepository repository;

    @Override
    public ApiResponse getAllCountriesCitiesAndRegions() {
        return new ApiResponse(true, "success", repository.findAll());
    }
}
