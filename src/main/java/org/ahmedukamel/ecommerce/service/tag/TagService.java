package org.ahmedukamel.ecommerce.service.tag;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

import java.util.List;

public interface TagService {
    ApiResponse getAllTags();

    ApiResponse getPosts(List<String> tagNames, int number);

    ApiResponse getProducts(List<String> tagNames, int number);
}