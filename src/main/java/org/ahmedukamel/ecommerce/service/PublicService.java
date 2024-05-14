package org.ahmedukamel.ecommerce.service;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

import java.io.IOException;

public interface PublicService {
    ApiResponse getAllCategories();

    ApiResponse getAllMainCategories();

    ApiResponse getAllMainAndSubCategories();

    ApiResponse getMainCategoryCategories(Integer mainCategoryId);

    ApiResponse getAllCountries();

    ApiResponse getMainCategoryById(Integer id);

    ApiResponse getCategoryById(Integer id);

    byte[] viewPicture(String picturePath) throws IOException;
}
