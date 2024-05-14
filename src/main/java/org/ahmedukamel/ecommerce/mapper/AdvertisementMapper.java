package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.AdvertisementDto;
import org.ahmedukamel.ecommerce.dto.AdvertisementDtoV2;
import org.ahmedukamel.ecommerce.model.Advertisement;
import org.ahmedukamel.ecommerce.model.AdvertisementDetail;
import org.ahmedukamel.ecommerce.model.BlogPostDetail;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class AdvertisementMapper {
    public static AdvertisementDto toResponse(Advertisement advertisement, Integer languageId) {
        AdvertisementDetail advertisementDetail = EntityDetailsUtils.supplyAdvertisementDetail(advertisement, languageId);
        AdvertisementDto response = new AdvertisementDto();
        response.setAdId(advertisement.getAdId());
        response.setStartDate(advertisement.getStartDate().toString());
        response.setEndDate(advertisement.getEndDate().toString());
        response.setImgUrl(advertisement.getImgUrl());
        response.setTargetUrl(advertisement.getTargetUrl());
        response.setStatus(advertisement.getStatus().name());
        response.setTitle(advertisementDetail.getTitle());
        response.setDescription(advertisementDetail.getDescription());
        return response;
    }

    public static List<AdvertisementDto> toResponse(Collection<Advertisement> items, Integer languageId) {
        return items.stream().map(item -> toResponse(item, languageId)).toList();
    }

    public static AdvertisementDtoV2 toResponseV2(Advertisement advertisement) {
        Function<String, AdvertisementDetail> function = (s) -> advertisement.getAdvertisementDetails().stream()
                .filter(i -> i.getLanguage().getCode().equalsIgnoreCase(s.strip()))
                .findFirst().orElseGet(AdvertisementDetail::new);

        AdvertisementDetail en = function.apply("en"),
                ar = function.apply("ar"),
                fr = function.apply("fr");

        AdvertisementDtoV2 response = new AdvertisementDtoV2();
        response.setTitle(en.getTitle());
        response.setDescription(en.getDescription());
        response.setArabicTitle(ar.getTitle());
        response.setArabicDescription(ar.getDescription());
        response.setFrenchTitle(fr.getTitle());
        response.setFrenchDescription(fr.getDescription());
        response.setImgUrl(advertisement.getImgUrl());
        response.setTargetUrl(advertisement.getTargetUrl());
        response.setAdId(advertisement.getAdId());
        response.setStartDate(advertisement.getStartDate().toString());
        response.setEndDate(advertisement.getEndDate().toString());
        response.setStatus(advertisement.getStatus().name());
        return response;
    }
}
