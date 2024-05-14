package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MainCategoryDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    // Data for response
    private Integer categoryId;
}
