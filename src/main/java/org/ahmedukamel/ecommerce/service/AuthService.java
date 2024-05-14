package org.ahmedukamel.ecommerce.service;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

public interface AuthService {
    ApiResponse register(Object request);

    ApiResponse login(Object request);
}
