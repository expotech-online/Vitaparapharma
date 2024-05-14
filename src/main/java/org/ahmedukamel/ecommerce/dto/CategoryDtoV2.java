package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDtoV2 extends CategoryDto {
    @NotBlank
    private String arabicName;
    @NotBlank
    private String arabicDescription;
    @NotBlank
    private String frenchName;
    @NotBlank
    private String frenchDescription;
}
