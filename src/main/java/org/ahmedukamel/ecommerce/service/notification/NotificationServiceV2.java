package org.ahmedukamel.ecommerce.service.notification;

import org.ahmedukamel.ecommerce.dto.request.NotificationRequestV2;
import org.ahmedukamel.ecommerce.dto.request.NotificationUsersRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

public interface NotificationServiceV2 {
    ApiResponse notifyAllCustomers(NotificationRequestV2 request);

    ApiResponse notifyCustomers(NotificationUsersRequest request);

    ApiResponse updateNotification(NotificationRequestV2 request, Integer notificationId);
}
