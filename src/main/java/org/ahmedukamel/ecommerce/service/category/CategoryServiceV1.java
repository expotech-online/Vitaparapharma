package org.ahmedukamel.ecommerce.service.category;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.CategoryDto;
import org.ahmedukamel.ecommerce.model.Category;
import org.ahmedukamel.ecommerce.model.CategoryDetail;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.model.MainCategory;
import org.ahmedukamel.ecommerce.repository.CategoryRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.MainCategoryRepository;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceV1 implements CategoryService {
    final CategoryRepository categoryRepository;
    final LanguageRepository languageRepository;
    final MessageSourceUtils messageSourceUtils;
    final MainCategoryRepository mainCategoryRepository;

    @Override
    public ApiResponse addCategory(CategoryDto request) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        MainCategory mainCategory = RepositoryUtils.getMainCategory(mainCategoryRepository, request.getMainCategoryId());
        // Processing
        Category category = new Category();
        CategoryDetail categoryDetail = new CategoryDetail();
        categoryDetail.setCategory(category);
        categoryDetail.setLanguage(language);
        category.setMainCategory(mainCategory);
        UpdateUtils.updateCategoryDetail(request, categoryDetail);
        category.getCategoryDetails().add(categoryDetail);
        categoryRepository.save(category);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.category");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updateCategory(CategoryDto request, Integer categoryId) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Category category = RepositoryUtils.getCategory(categoryRepository, categoryId);
        // Validating
        MainCategory mainCategory = ValidationUtils.validateGetMainCategory(mainCategoryRepository, request);
        // Processing
        CategoryDetail categoryDetail = EntityDetailsUtils.supplyCategoryDetail(category, language.getLanguageId());
        categoryDetail.setCategory(category);
        categoryDetail.setLanguage(language);
        UpdateUtils.updateMainCategory(category, mainCategory);
        UpdateUtils.updateCategoryDetail(request, categoryDetail);
        category.getCategoryDetails().add(categoryDetail);
        categoryRepository.save(category);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.category");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse deleteCategory(Integer categoryId) {
        // Querying
        Category category = RepositoryUtils.getCategory(categoryRepository, categoryId);
        // Validating
        ValidationUtils.validateEmptyCategory(category.getProducts(), messageSourceUtils);
        // Processing
        categoryRepository.delete(category);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.category");
        return new ApiResponse(true, message);
    }
}