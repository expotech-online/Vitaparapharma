package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank
    private String description;
    @NotNull
    @Min(value = 1)
    private Integer regionId;
    @NotNull
    @Min(value = 9999)
    private Integer zipCode;
}