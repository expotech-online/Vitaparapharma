package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ahmedukamel.ecommerce.validation.annotation.ValidMainCategory;

@Data
public class CategoryDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @ValidMainCategory
    private Integer mainCategoryId;
    // Data for response
    private Integer categoryId;
}
