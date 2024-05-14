package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.response.NotificationResponse;
import org.ahmedukamel.ecommerce.dto.response.NotificationResponseV3;
import org.ahmedukamel.ecommerce.model.CustomerNotification;
import org.ahmedukamel.ecommerce.model.Notification;
import org.ahmedukamel.ecommerce.model.NotificationDetail;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;
import org.ahmedukamel.ecommerce.util.LocalizedEnumUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class NotificationMapper {
    public static NotificationResponse toResponse(CustomerNotification customerNotification, Integer languageId) {
        Notification notification = customerNotification.getNotification();
        NotificationDetail notificationDetail = EntityDetailsUtils.supplyNotificationDetail(notification, languageId);
        NotificationResponse response = new NotificationResponse();
        response.setMessage(notificationDetail.getMessage());
        response.setTime(notification.getTimestamp().toString());
        return response;
    }

    public static List<NotificationResponse> toResponse(Collection<CustomerNotification> items, Integer languageId) {
        return items.stream()
                .map(item -> toResponse(item, languageId))
                .sorted(Comparator.comparing(NotificationResponse::getTime).reversed())
                .toList();
    }

    public static NotificationResponseV3 toResponseV3(CustomerNotification customerNotification, Integer languageId, LocalizedEnumUtils localizedEnumUtils) {
        NotificationResponse response = toResponse(customerNotification, languageId);
        NotificationResponseV3 notificationResponseV3 = new NotificationResponseV3();
        BeanUtils.copyProperties(response, notificationResponseV3);
        NotificationType notificationType = customerNotification.getNotification().getType();
        notificationResponseV3.setIdentifier(customerNotification.getNotification().getIdentifier());
        notificationResponseV3.setType(localizedEnumUtils.getNotificationType(notificationType));
        notificationResponseV3.setTypeId(notificationType.getId());
        notificationResponseV3.setId(customerNotification.getId());
        notificationResponseV3.setRead(customerNotification.isRead());
        return notificationResponseV3;
    }

    public static List<NotificationResponseV3> toResponseV3(Collection<CustomerNotification> items, Integer languageId, LocalizedEnumUtils localizedEnumUtils) {
        return items.stream()
                .map(item -> toResponseV3(item, languageId, localizedEnumUtils))
                .sorted(Comparator.comparing(NotificationResponse::getTime).reversed())
                .toList();
    }
}
