package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ahmedukamel.ecommerce.dto.CategoryDto;
import org.ahmedukamel.ecommerce.dto.MainCategoryDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MainSubCategoryResponse extends MainCategoryDto {
    private List<CategoryDto> subCategories;
}
