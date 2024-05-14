package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertisementDtoV2 extends AdvertisementDto {
    @NotBlank
    private String arabicTitle;
    @NotBlank
    private String arabicDescription;
    @NotBlank
    private String frenchTitle;
    @NotBlank
    private String frenchDescription;
}
