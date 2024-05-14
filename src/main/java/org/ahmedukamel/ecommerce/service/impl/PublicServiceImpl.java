package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.*;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.MainSubCategoryResponse;
import org.ahmedukamel.ecommerce.mapper.*;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.Country;
import org.ahmedukamel.ecommerce.repository.*;
import org.ahmedukamel.ecommerce.service.PublicService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getCategory;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getMainCategory;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicServiceImpl implements PublicService {
    private final MainCategoryRepository mainCategoryRepository;
    private final LocalizedEnumUtils localizedEnumUtils;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;

    @Override
    public ApiResponse getAllMainCategories() {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        List<MainCategory> mainCategoryList = mainCategoryRepository.findAll();
        // Processing
        List<MainCategoryDto> mainCategoryDtoList = MainCategoryMapper.toResponse(mainCategoryList, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.main.categories");
        return new ApiResponse(true, message, Map.of("mainCategories", mainCategoryDtoList));
    }

    @Override
    public ApiResponse getAllMainAndSubCategories() {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        List<MainCategory> mainCategoryList = mainCategoryRepository.findAll();
        // Processing
        List<MainSubCategoryResponse> mainCategoryDtoList = MainCategoryMapper.toMainSubResponse(mainCategoryList, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.main.categories");
        return new ApiResponse(true, message, Map.of("mainCategories", mainCategoryDtoList));
    }

    @Override
    public ApiResponse getMainCategoryCategories(Integer mainCategoryId) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        MainCategory mainCategory = getMainCategory(mainCategoryRepository, mainCategoryId);
        // Processing
        Collection<Category> categories = mainCategory.getCategories();
        List<CategoryDto> categoryDtoList = CategoryMapper.toResponse(categories, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.main.category");
        return new ApiResponse(true, message, Map.of("categories", categoryDtoList));
    }

    @Override
    public ApiResponse getAllCategories() {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        List<Category> categories = categoryRepository.findAll();
        // Processing
        List<CategoryDto> categoryDtoList = CategoryMapper.toResponse(categories, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.categories");
        return new ApiResponse(true, message, Map.of("categories", categoryDtoList));
    }

    @Override
    public ApiResponse getAllCountries() {
        Map<String, String> countries = Arrays.stream(Country.values()).collect(Collectors.toMap(Country::name, localizedEnumUtils::getCountry));
        return new ApiResponse(true, "", Map.of("countries", countries));
    }

    @Override
    public ApiResponse getMainCategoryById(Integer id) {
        MainCategory mainCategory = getMainCategory(mainCategoryRepository, id);
        MainCategoryDtoV2 data = MainCategoryMapper.toResponseV2(mainCategory);
        return new ApiResponse(true, "success", Map.of("category", data));
    }

    @Override
    public ApiResponse getCategoryById(Integer id) {
        Category category = getCategory(categoryRepository, id);
        CategoryDtoV2 data = CategoryMapper.toResponseV2(category);
        return new ApiResponse(true, "success", Map.of("category", data));
    }

    @Override
    public byte[] viewPicture(String imageName) throws IOException {
        return Files.readAllBytes(ImageDirectoryUtils.getImagePath(imageName));
    }
}