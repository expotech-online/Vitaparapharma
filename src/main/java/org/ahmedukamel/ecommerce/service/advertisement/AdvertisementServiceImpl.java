package org.ahmedukamel.ecommerce.service.advertisement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.AdvertisementDto;
import org.ahmedukamel.ecommerce.dto.request.AdStatusRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.AdvertisementMapper;
import org.ahmedukamel.ecommerce.model.Advertisement;
import org.ahmedukamel.ecommerce.model.AdvertisementDetail;
import org.ahmedukamel.ecommerce.model.enumeration.AdvertisementStatus;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.repository.AdvertisementRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AdvertisementServiceImpl implements AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final LanguageRepository languageRepository;
    private final LocalizedEnumUtils localizedEnumUtils;
    private final MessageSourceUtils messageSourceUtils;

    @Override
    public ApiResponse addAdvertisement(AdvertisementDto request) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        Advertisement advertisement = new Advertisement();
        advertisement.setStatus(AdvertisementStatus.PENDING);
        advertisement.setImgUrl("");
        AdvertisementDetail advertisementDetail = new AdvertisementDetail();
        advertisementDetail.setAdvertisement(advertisement);
        advertisementDetail.setLanguage(language);
        UpdateUtils.updateAdvertisement(request, advertisement);
        UpdateUtils.updateAdvertisementDetail(request, advertisementDetail);
        advertisement.getAdvertisementDetails().add(advertisementDetail);
        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.advertisement");
        return new ApiResponse(true, message, Map.of("advertisementId", savedAdvertisement.getAdId()));
    }

    @Override
    public ApiResponse updateAdvertisement(Integer advertisementId, AdvertisementDto request) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Advertisement advertisement = RepositoryUtils.getAdvertisement(advertisementRepository, advertisementId);
        // Processing
        AdvertisementDetail advertisementDetail = EntityDetailsUtils.supplyAdvertisementDetail(advertisement, language.getLanguageId());
        advertisementDetail.setAdvertisement(advertisement);
        advertisementDetail.setLanguage(language);
        UpdateUtils.updateAdvertisement(request, advertisement);
        UpdateUtils.updateAdvertisementDetail(request, advertisementDetail);
        advertisement.getAdvertisementDetails().add(advertisementDetail);
        advertisementRepository.save(advertisement);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.advertisement");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse deleteAdvertisement(Integer advertisementId) {
        // Querying
        Advertisement advertisement = RepositoryUtils.getAdvertisement(advertisementRepository, advertisementId);
        // Processing
        ImageDirectoryUtils.deleteImage(advertisement.getImgUrl());
        advertisementRepository.delete(advertisement);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.advertisement");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getAdvertisementStatuses() {
        // Processing
        List<String> statuses = Arrays.stream(AdvertisementStatus.values())
                .map(i -> localizedEnumUtils.getAdvertisementStatus(i.name())).toList();
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.advertisement.statuses");
        return new ApiResponse(true, message, Map.of("statuses", statuses));
    }

    @Override
    public ApiResponse setAdvertisementStatus(AdStatusRequest request) {
        // Querying
        Advertisement advertisement = RepositoryUtils.getAdvertisement(advertisementRepository, request.getAdvertisementId());
        // Processing
        AdvertisementStatus status = AdvertisementStatus.valueOf(request.getAdvertisementStatus().toUpperCase());
        advertisement.setStatus(status);
        advertisementRepository.save(advertisement);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.advertisement.status");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getAdvertisement(Integer advertisementId) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Advertisement advertisement = RepositoryUtils.getAdvertisement(advertisementRepository, advertisementId);
        // Processing
        AdvertisementDto advertisementResponse = AdvertisementMapper.toResponse(advertisement, language.getLanguageId());
        localizeAdvertisementStatus(advertisementResponse);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.advertisement");
        return new ApiResponse(true, message, Map.of("advertisement", advertisementResponse));
    }

    @Override
    public ApiResponse getAllAdvertisements() {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        List<Advertisement> advertisementList = advertisementRepository.findAll();
        List<AdvertisementDto> advertisementResponseList = AdvertisementMapper.toResponse(advertisementList, language.getLanguageId());
        advertisementResponseList.forEach(this::localizeAdvertisementStatus);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.advertisements");
        return new ApiResponse(true, message, Map.of("advertisements", advertisementResponseList));
    }

    @Override
    public ApiResponse getActiveAdvertisements() {
        return getAdvertisements(AdvertisementStatus.ACTIVE);
    }

    @Override
    public ApiResponse getInactiveAdvertisements() {
        return getAdvertisements(AdvertisementStatus.INACTIVE);
    }

    @Override
    public ApiResponse getPendingAdvertisements() {
        return getAdvertisements(AdvertisementStatus.PENDING);
    }

    @Override
    public ApiResponse getPausedAdvertisements() {
        return getAdvertisements(AdvertisementStatus.PAUSED);
    }

    @Override
    public ApiResponse getExpiredAdvertisements() {
        return getAdvertisements(AdvertisementStatus.EXPIRED);
    }

    @Override
    public ApiResponse uploadAdvertisementPicture(Integer advertisementId, MultipartFile image) throws IOException {
        // Querying
        Advertisement advertisement = RepositoryUtils.getAdvertisement(advertisementRepository, advertisementId);
        // Validating
        ValidationUtils.validateImage(image, messageSourceUtils);
        ValidationUtils.validateNotExistAdvertisementPicture(advertisement, messageSourceUtils);
        // Processing
        String imageName = ImageDirectoryUtils.saveImage(image);
        advertisement.setImgUrl(ImageDirectoryUtils.getImageUrl(imageName));
        advertisementRepository.save(advertisement);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.upload.advertisement.picture");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse deleteAdvertisementPicture(Integer advertisementId) {
        // Querying
        Advertisement advertisement = RepositoryUtils.getAdvertisement(advertisementRepository, advertisementId);
        // Validating
        ValidationUtils.validateExistAdvertisementPicture(advertisement, messageSourceUtils);
        // Processing
        ImageDirectoryUtils.deleteImage(advertisement.getImgUrl());
        advertisement.setImgUrl("");
        advertisementRepository.save(advertisement);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.advertisement.picture");
        return new ApiResponse(true, message);
    }

    private ApiResponse getAdvertisements(AdvertisementStatus status) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        List<Advertisement> advertisementList = advertisementRepository.findAllByStatus(status);
        List<AdvertisementDto> advertisementResponseList = AdvertisementMapper.toResponse(advertisementList, language.getLanguageId());
        advertisementResponseList.forEach(this::localizeAdvertisementStatus);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.advertisements");
        return new ApiResponse(true, message, Map.of("advertisements", advertisementResponseList));
    }

    private void localizeAdvertisementStatus(AdvertisementDto response) {
        response.setStatus(localizedEnumUtils.getAdvertisementStatus(response.getStatus()));
    }
}