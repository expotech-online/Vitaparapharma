package org.ahmedukamel.ecommerce.service.advertisement;

import org.ahmedukamel.ecommerce.dto.AdvertisementDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdvertisementServiceV2 {
    ApiResponse addAdvertisement(AdvertisementDtoV2 request, MultipartFile image) throws IOException;

    ApiResponse updateAdvertisement(AdvertisementDtoV2 request, int id);

    ApiResponse getAdvertisementById(int id);
}
