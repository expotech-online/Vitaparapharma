package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.MainCategoryDto;
import org.ahmedukamel.ecommerce.dto.MainCategoryDtoV2;
import org.ahmedukamel.ecommerce.dto.response.MainSubCategoryResponse;
import org.ahmedukamel.ecommerce.model.MainCategory;
import org.ahmedukamel.ecommerce.model.MainCategoryDetail;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class MainCategoryMapper {
    public static MainCategoryDto toResponse(MainCategory mainCategory, Integer languageId) {
        MainCategoryDetail mainCategoryDetail = EntityDetailsUtils.supplyMainCategoryDetail(mainCategory, languageId);
        MainCategoryDto response = new MainCategoryDto();
        response.setCategoryId(mainCategory.getCategoryId());
        response.setName(mainCategoryDetail.getName());
        response.setDescription(mainCategoryDetail.getDescription());
        return response;
    }

    public static List<MainCategoryDto> toResponse(Collection<MainCategory> items, Integer languageId) {
        return items.stream().map(item -> toResponse(item, languageId)).toList();
    }

    public static MainCategoryDtoV2 toResponseV2(MainCategory mainCategory) {
        MainCategoryDtoV2 response = new MainCategoryDtoV2();

        Function<String, MainCategoryDetail> function =
                (s) -> mainCategory.getMainCategoryDetails().stream()
                        .filter(i -> i.getLanguage().getCode().equalsIgnoreCase(s.strip()))
                        .findFirst().orElseGet(MainCategoryDetail::new);

        MainCategoryDetail en = function.apply("en"),
                ar = function.apply("ar"),
                fr = function.apply("fr");

        response.setCategoryId(mainCategory.getCategoryId());
        response.setName(en.getName());
        response.setDescription(en.getDescription());
        response.setArabicName(ar.getName());
        response.setArabicDescription(ar.getDescription());
        response.setFrenchName(fr.getName());
        response.setFrenchDescription(fr.getDescription());

        return response;
    }

    // TODO
    public static List<MainCategoryDtoV2> toResponseV2(Collection<MainCategory> mainCategories) {
        return mainCategories.stream().map(MainCategoryMapper::toResponseV2).toList();
    }

    public static MainSubCategoryResponse toMainSubResponse(MainCategory mainCategory, Integer languageId) {
        MainCategoryDetail mainCategoryDetail = EntityDetailsUtils.supplyMainCategoryDetail(mainCategory, languageId);
        MainSubCategoryResponse response = new MainSubCategoryResponse();
        response.setCategoryId(mainCategory.getCategoryId());
        response.setName(mainCategoryDetail.getName());
        response.setDescription(mainCategoryDetail.getDescription());
        response.setSubCategories(CategoryMapper.toResponse(mainCategory.getCategories(), languageId));
        return response;
    }

    public static List<MainSubCategoryResponse> toMainSubResponse(Collection<MainCategory> items, Integer languageId) {
        return items.stream().map(item -> toMainSubResponse(item, languageId)).toList();
    }
}
