package org.ahmedukamel.ecommerce.service;

import org.ahmedukamel.ecommerce.dto.request.AddressRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

public interface AddressService {
    ApiResponse addAddress(Object request);

    ApiResponse deleteAddress(Integer addressId);

    ApiResponse getAddresses();
}
