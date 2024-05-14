package org.ahmedukamel.ecommerce.service.advertisement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.AdvertisementDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.AdvertisementMapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.AdvertisementStatus;
import org.ahmedukamel.ecommerce.repository.AdvertisementRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.util.DateTimeUtils;
import org.ahmedukamel.ecommerce.util.ImageDirectoryUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.ahmedukamel.ecommerce.util.EntityDetailsUtils.supplyAdvertisementDetail;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getLanguage;

@Service
@RequiredArgsConstructor
@Transactional
public class AdvertisementServiceV2Impl implements AdvertisementServiceV2 {
    final AdvertisementRepository repository;
    final LanguageRepository languageRepository;
    final MessageSourceUtils messageSourceUtils;

    @Override
    public ApiResponse addAdvertisement(AdvertisementDtoV2 request, MultipartFile image) throws IOException {
        Advertisement advertisement = new Advertisement();
        setAdvertisementValue(advertisement, request);
        String imageName = ImageDirectoryUtils.saveImage(image);
        advertisement.setImgUrl(ImageDirectoryUtils.getImageUrl(imageName));
        advertisement.setStatus(AdvertisementStatus.PENDING);
        repository.save(advertisement);
        String message = messageSourceUtils.getMessage("operation.successful.add.advertisement");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updateAdvertisement(AdvertisementDtoV2 request, int id) {
        Advertisement advertisement = RepositoryUtils.getAdvertisement(repository, id);
        setAdvertisementValue(advertisement, request);
        repository.save(advertisement);
        String message = messageSourceUtils.getMessage("operation.successful.update.advertisement");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getAdvertisementById(int id) {
        Advertisement advertisement = RepositoryUtils.getAdvertisement(repository, id);
        AdvertisementDtoV2 response = AdvertisementMapper.toResponseV2(advertisement);
        String message = messageSourceUtils.getMessage("operation.successful.get.advertisement");
        return new ApiResponse(true, message, Map.of("advertisement", response));
    }

    private void setAdvertisementValue(Advertisement advertisement, AdvertisementDtoV2 request) {
        Language enL = getLanguage(languageRepository, "en"),
                arL = getLanguage(languageRepository, "ar"),
                frL = getLanguage(languageRepository, "fr");

        AdvertisementDetail en = supplyAdvertisementDetail(advertisement, enL.getLanguageId()),
                ar = supplyAdvertisementDetail(advertisement, arL.getLanguageId()),
                fr = supplyAdvertisementDetail(advertisement, frL.getLanguageId());

        en.setTitle(request.getTitle());
        en.setDescription(request.getDescription());
        en.setAdvertisement(advertisement);
        en.setLanguage(enL);

        ar.setTitle(request.getArabicTitle());
        ar.setDescription(request.getArabicDescription());
        ar.setAdvertisement(advertisement);
        ar.setLanguage(arL);

        fr.setTitle(request.getFrenchTitle());
        fr.setDescription(request.getFrenchDescription());
        fr.setAdvertisement(advertisement);
        fr.setLanguage(frL);

        advertisement.getAdvertisementDetails().addAll(List.of(en, ar, fr));
        advertisement.setEndDate(DateTimeUtils.getDateTime(request.getEndDate()));
        advertisement.setStartDate(DateTimeUtils.getDateTime(request.getStartDate()));
        advertisement.setTargetUrl(request.getTargetUrl());
    }
}
