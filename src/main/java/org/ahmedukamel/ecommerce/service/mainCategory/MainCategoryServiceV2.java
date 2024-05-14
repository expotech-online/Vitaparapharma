package org.ahmedukamel.ecommerce.service.mainCategory;

import org.ahmedukamel.ecommerce.dto.MainCategoryDto;
import org.ahmedukamel.ecommerce.dto.MainCategoryDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.model.MainCategory;
import org.ahmedukamel.ecommerce.model.MainCategoryDetail;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.MainCategoryRepository;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ahmedukamel.ecommerce.util.EntityDetailsUtils.supplyMainCategoryDetail;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getLanguage;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getMainCategory;

@Service
public class MainCategoryServiceV2 extends MainCategoryServiceV1 {
    public MainCategoryServiceV2(MainCategoryRepository mainCategoryRepository,
                                 MessageSourceUtils messageSourceUtils,
                                 LanguageRepository languageRepository) {
        super(mainCategoryRepository, messageSourceUtils, languageRepository);
    }

    @Override
    public ApiResponse addMainCategory(MainCategoryDto dto) {
        MainCategoryDtoV2 request = (MainCategoryDtoV2) dto;
        MainCategory mainCategory = new MainCategory();
        setMainCategoryValues(request, mainCategory);
        String message = messageSourceUtils.getMessage("operation.successful.add.main.category");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updateMainCategory(MainCategoryDto dto, Integer mainCategoryId) {
        MainCategoryDtoV2 request = (MainCategoryDtoV2) dto;
        MainCategory mainCategory = getMainCategory(mainCategoryRepository, mainCategoryId);
        setMainCategoryValues(request, mainCategory);
        String message = messageSourceUtils.getMessage("operation.successful.update.main.category");
        return new ApiResponse(true, message);
    }

    protected void setMainCategoryValues(MainCategoryDtoV2 request, MainCategory mainCategory) {
        Language enL = getLanguage(languageRepository, "en"),
                arL = getLanguage(languageRepository, "ar"),
                frL = getLanguage(languageRepository, "fr");

        MainCategoryDetail en = supplyMainCategoryDetail(mainCategory, enL.getLanguageId()),
                ar = supplyMainCategoryDetail(mainCategory, arL.getLanguageId()),
                fr = supplyMainCategoryDetail(mainCategory, frL.getLanguageId());

        en.setName(request.getName());
        en.setDescription(request.getDescription());
        en.setLanguage(enL);
        en.setMainCategory(mainCategory);

        ar.setName(request.getArabicName());
        ar.setDescription(request.getArabicDescription());
        ar.setLanguage(arL);
        ar.setMainCategory(mainCategory);

        fr.setName(request.getFrenchName());
        fr.setDescription(request.getFrenchDescription());
        fr.setLanguage(frL);
        fr.setMainCategory(mainCategory);

        mainCategory.getMainCategoryDetails().addAll(List.of(en, ar, fr));
        mainCategoryRepository.save(mainCategory);
    }
}
