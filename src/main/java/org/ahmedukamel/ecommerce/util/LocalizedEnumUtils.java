package org.ahmedukamel.ecommerce.util;

import lombok.AllArgsConstructor;
import org.ahmedukamel.ecommerce.model.enumeration.*;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class LocalizedEnumUtils {
    private MessageSourceUtils messageSourceUtils;

    public String getCountry(Country country) {
        return messageSourceUtils.getMessage("enumeration.country." + country.name() + ".name");
    }

    public String getCountry(String country) {
        return messageSourceUtils.getMessage("enumeration.country." + country + ".name");
    }

    public String getRole(Role role) {
        return messageSourceUtils.getMessage("enumeration.role." + role.name() + ".name");
    }

    public String getReportType(ReportType type) {
        return messageSourceUtils.getMessage("enumeration.report.type." + type.name() + ".name");
    }

    public String getOrderStatus(OrderStatus status) {
        return messageSourceUtils.getMessage("enumeration.order.status." + status.name() + ".name");
    }

    public String getOrderStatus(String status, Locale locale) {
        return messageSourceUtils.getMessageByLocale("enumeration.order.status." + status + ".name", locale);
    }

    public String getAdvertisementStatus(String status) {
        return messageSourceUtils.getMessage("enumeration.advertisement.status." + status + ".name");
    }

    public String getNotificationType(NotificationType type) {
        return messageSourceUtils.getMessage("enumeration.notification.type." + type.name() + ".name");
    }
}
