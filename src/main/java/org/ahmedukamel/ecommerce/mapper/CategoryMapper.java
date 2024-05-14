package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.CategoryDto;
import org.ahmedukamel.ecommerce.dto.CategoryDtoV2;
import org.ahmedukamel.ecommerce.model.Category;
import org.ahmedukamel.ecommerce.model.CategoryDetail;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class CategoryMapper {
    public static CategoryDto toResponse(Category category, Integer languageId) {
        CategoryDetail categoryDetail = EntityDetailsUtils.supplyCategoryDetail(category, languageId);
        CategoryDto response = new CategoryDto();
        response.setCategoryId(category.getCategoryId());
        response.setName(categoryDetail.getName());
        response.setDescription(categoryDetail.getDescription());
        response.setMainCategoryId(category.getMainCategory().getCategoryId());
        return response;
    }

    public static List<CategoryDto> toResponse(Collection<Category> items, Integer languageId) {
        return items.stream().map(item -> CategoryMapper.toResponse(item, languageId)).toList();
    }

    public static CategoryDtoV2 toResponseV2(Category category) {
        CategoryDtoV2 response = new CategoryDtoV2();

        Function<String, CategoryDetail> function =
                (s) -> category.getCategoryDetails().stream()
                        .filter(i -> i.getLanguage().getCode().equalsIgnoreCase(s.strip()))
                        .findFirst().orElseGet(CategoryDetail::new);

        CategoryDetail en = function.apply("en"),
                ar = function.apply("ar"),
                fr = function.apply("fr");

        response.setCategoryId(category.getCategoryId());
        response.setName(en.getName());
        response.setDescription(en.getDescription());
        response.setArabicName(ar.getName());
        response.setArabicDescription(ar.getDescription());
        response.setFrenchName(fr.getName());
        response.setFrenchDescription(fr.getDescription());
        response.setMainCategoryId(category.getMainCategory().getCategoryId());

        return response;
    }
}
