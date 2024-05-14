package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.response.AddressResponse;
import org.ahmedukamel.ecommerce.model.Address;
import org.ahmedukamel.ecommerce.util.LocaleContextUtils;

import java.util.Collection;
import java.util.List;

public class AddressMapper {
    public static AddressResponse toResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setAddressId(address.getAddressId());
        response.setZipCode(address.getZipCode());
        response.setDescription(address.getDescription());
        response.setCountry(getCountryName(address));
        response.setCity(getCityName(address));
        response.setRegion(getRegionName(address));
        return response;
    }

    public static List<AddressResponse> toResponse(Collection<Address> items) {
        return items.stream().map(AddressMapper::toResponse).toList();
    }

    public static String getCountryName(Address address) {
        String languageCode = LocaleContextUtils.getLanguage();
        return switch (languageCode) {
            case "en" -> address.getRegion().getCity().getCountry().getEn();
            case "ar" -> address.getRegion().getCity().getCountry().getAr();
            case "fr" -> address.getRegion().getCity().getCountry().getFr();
            default -> "";
        };
    }

    public static String getCityName(Address address) {
        String languageCode = LocaleContextUtils.getLanguage();
        return switch (languageCode) {
            case "en" -> address.getRegion().getCity().getEn();
            case "ar" -> address.getRegion().getCity().getAr();
            case "fr" -> address.getRegion().getCity().getFr();
            default -> "";
        };
    }

    public static String getRegionName(Address address) {
        String languageCode = LocaleContextUtils.getLanguage();
        return switch (languageCode) {
            case "en" -> address.getRegion().getEn();
            case "ar" -> address.getRegion().getAr();
            case "fr" -> address.getRegion().getFr();
            default -> "";
        };
    }
}
