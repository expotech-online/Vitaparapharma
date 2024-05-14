package org.ahmedukamel.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.enumeration.*;
import org.ahmedukamel.ecommerce.util.LocalizedEnumUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnumService {
    final LocalizedEnumUtils localizedEnumUtils;

    public ApiResponse getOrderStatues() {
        Map<String, String> data = Arrays.stream(OrderStatus.values()).collect(Collectors.toMap(Enum::name, localizedEnumUtils::getOrderStatus));
        return new ApiResponse(true, "Order Statuses have been returned successfully.", Map.of("statues", data));
    }

    public ApiResponse getCouponsDiscountType() {
        List<String> data = Arrays.stream(DiscountType.values()).map(Enum::name).toList();
        return new ApiResponse(true, "Coupon discount types have been returned successfully.", Map.of("types", data));
    }

    public ApiResponse getCouponType() {
        List<String> data = Arrays.stream(CouponType.values()).map(Enum::name).toList();
        return new ApiResponse(true, "Coupon types have been returned successfully.", Map.of("types", data));
    }

    public ApiResponse getNotificationType() {
        Map<String, Integer> data = Arrays.stream(NotificationType.values()).collect(Collectors.toMap(Enum::name, NotificationType::getId));
        return new ApiResponse(true, "Notification types have been returned successfully.", Map.of("types", data));
    }

    public ApiResponse getAdvertisementStatuses() {
        List<String> statuses = Arrays.stream(AdvertisementStatus.values()).map(i -> localizedEnumUtils.getAdvertisementStatus(i.name())).toList();
        return new ApiResponse(true, "Advertisement statuses have been returned successfully.", Map.of("statuses", statuses));
    }

    public ApiResponse getReportTypes() {
        Map<String, String> reportTypeMap = Arrays.stream(ReportType.values()).collect(Collectors.toMap(ReportType::name, localizedEnumUtils::getReportType));
        return new ApiResponse(true, "Review report types have been returned successfully.", Map.of("types", reportTypeMap));
    }

    public ApiResponse getRoles() {
        Map<String, String> roles = Arrays.stream(Role.values()).collect(Collectors.toMap(Role::name, localizedEnumUtils::getRole));
        return new ApiResponse(true, "Roles have been returned successfully.", Map.of("roles", roles));
    }

    public ApiResponse getAllCountries() {
        Map<String, String> countries = Arrays.stream(Country.values()).collect(Collectors.toMap(Country::name, localizedEnumUtils::getCountry));
        return new ApiResponse(true, "Countries have been returned successfully.", Map.of("countries", countries));
    }
}
