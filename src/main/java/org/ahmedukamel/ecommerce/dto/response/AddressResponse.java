package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

@Data
public class AddressResponse {
    private Integer addressId;
    private String country;
    private String city;
    private String region;
    private String description;
    private Integer zipCode;
}
