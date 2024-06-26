package org.ahmedukamel.ecommerce.service;

import org.ahmedukamel.ecommerce.dto.request.RoleRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

public interface SuperAdminService {
    ApiResponse setCustomerRole(RoleRequest request);

    ApiResponse getRoles();

    ApiResponse setCustomerDisable(Integer customerId);

    ApiResponse setCustomerEnable(Integer customerId);
}
