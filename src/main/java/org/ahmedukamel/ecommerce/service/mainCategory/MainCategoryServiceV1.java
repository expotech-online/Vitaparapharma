package org.ahmedukamel.ecommerce.service.mainCategory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.MainCategoryDto;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.model.MainCategory;
import org.ahmedukamel.ecommerce.model.MainCategoryDetail;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.MainCategoryRepository;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MainCategoryServiceV1 implements MainCategoryService {
     final MainCategoryRepository mainCategoryRepository;
     final MessageSourceUtils messageSourceUtils;
     final LanguageRepository languageRepository;

    @Override
    public ApiResponse addMainCategory(MainCategoryDto request) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        MainCategory mainCategory = new MainCategory();
        MainCategoryDetail mainCategoryDetail = new MainCategoryDetail();
        mainCategoryDetail.setMainCategory(mainCategory);
        mainCategoryDetail.setLanguage(language);
        UpdateUtils.updateMainCategoryDetails(request, mainCategoryDetail);
        mainCategory.getMainCategoryDetails().add(mainCategoryDetail);
        mainCategoryRepository.save(mainCategory);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.main.category");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updateMainCategory(MainCategoryDto request, Integer mainCategoryId) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        MainCategory mainCategory = RepositoryUtils.getMainCategory(mainCategoryRepository, mainCategoryId);
        // Processing
        MainCategoryDetail mainCategoryDetail = EntityDetailsUtils.supplyMainCategoryDetail(mainCategory, language.getLanguageId());
        mainCategoryDetail.setMainCategory(mainCategory);
        mainCategoryDetail.setLanguage(language);
        UpdateUtils.updateMainCategoryDetails(request, mainCategoryDetail);
        mainCategory.getMainCategoryDetails().add(mainCategoryDetail);
        mainCategoryRepository.save(mainCategory);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.main.category");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse deleteMainCategory(Integer mainCategoryId) {
        // Querying
        MainCategory mainCategory = RepositoryUtils.getMainCategory(mainCategoryRepository, mainCategoryId);
        // Validating
        ValidationUtils.validateMainCategoryCategories(mainCategory, messageSourceUtils);
        // Processing
        mainCategoryRepository.delete(mainCategory);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.main.category");
        return new ApiResponse(true, message);
    }
}
