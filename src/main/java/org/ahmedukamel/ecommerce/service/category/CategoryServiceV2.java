package org.ahmedukamel.ecommerce.service.category;

import org.ahmedukamel.ecommerce.dto.CategoryDto;
import org.ahmedukamel.ecommerce.dto.CategoryDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.Category;
import org.ahmedukamel.ecommerce.model.CategoryDetail;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.repository.CategoryRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.MainCategoryRepository;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ahmedukamel.ecommerce.util.EntityDetailsUtils.supplyCategoryDetail;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.*;

@Service
public class CategoryServiceV2 extends CategoryServiceV1 {
    public CategoryServiceV2(CategoryRepository categoryRepository, LanguageRepository languageRepository, MessageSourceUtils messageSourceUtils, MainCategoryRepository mainCategoryRepository) {
        super(categoryRepository, languageRepository, messageSourceUtils, mainCategoryRepository);
    }

    @Override
    public ApiResponse addCategory(CategoryDto dto) {
        CategoryDtoV2 request = (CategoryDtoV2) dto;
        Category category = new Category();
        setCategoryValue(category, request);
        String message = messageSourceUtils.getMessage("operation.successful.add.category");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updateCategory(CategoryDto dto, Integer categoryId) {
        CategoryDtoV2 request = (CategoryDtoV2) dto;
        Category category = getCategory(categoryRepository, categoryId);
        setCategoryValue(category, request);
        String message = messageSourceUtils.getMessage("operation.successful.update.category");
        return new ApiResponse(true, message);
    }

    protected void setCategoryValue(Category category, CategoryDtoV2 request) {
        Language enL = getLanguage(languageRepository, "en"), arL = getLanguage(languageRepository, "ar"), frL = getLanguage(languageRepository, "fr");

        CategoryDetail en = supplyCategoryDetail(category, enL.getLanguageId()), ar = supplyCategoryDetail(category, arL.getLanguageId()), fr = supplyCategoryDetail(category, frL.getLanguageId());

        en.setName(request.getName());
        en.setDescription(request.getDescription());
        en.setLanguage(enL);
        en.setCategory(category);

        ar.setName(request.getArabicName());
        ar.setDescription(request.getArabicDescription());
        ar.setLanguage(arL);
        ar.setCategory(category);

        fr.setName(request.getFrenchName());
        fr.setDescription(request.getFrenchDescription());
        fr.setLanguage(frL);
        fr.setCategory(category);

        category.setMainCategory(getMainCategory(mainCategoryRepository, request.getMainCategoryId()));
        category.getCategoryDetails().addAll(List.of(en, ar, fr));
        categoryRepository.save(category);
    }
}
