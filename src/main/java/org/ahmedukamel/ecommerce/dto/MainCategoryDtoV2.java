package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MainCategoryDtoV2 extends MainCategoryDto {
    @NotBlank
    private String arabicName;
    @NotBlank
    private String arabicDescription;
    @NotBlank
    private String frenchName;
    @NotBlank
    private String frenchDescription;
}
