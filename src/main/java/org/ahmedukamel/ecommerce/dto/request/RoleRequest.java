package org.ahmedukamel.ecommerce.dto.request;

import lombok.Data;
import org.ahmedukamel.ecommerce.validation.annotation.ValidCustomer;
import org.ahmedukamel.ecommerce.validation.annotation.ValidRole;

@Data
public class RoleRequest {
    @ValidRole
    private String role;
    @ValidCustomer
    private Integer customerId;
}
