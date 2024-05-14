package org.ahmedukamel.ecommerce.service.notification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.NotificationMsgRequest;
import org.ahmedukamel.ecommerce.dto.request.NotificationRequestV2;
import org.ahmedukamel.ecommerce.dto.request.NotificationUsersRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.Notification;
import org.ahmedukamel.ecommerce.model.NotificationDetail;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.NotificationRepository;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.List;

import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getCustomers;

@Service
@RequiredArgsConstructor
public class NotificationServiceV2Impl implements NotificationServiceV2 {
    final NotificationRepository notificationRepository;
    final CustomerRepository customerRepository;
    final MessageSourceUtils messageSourceUtils;
    final NotifyService notifyService;

    @Override
    public ApiResponse notifyAllCustomers(NotificationRequestV2 request) {
        NotificationMsgRequest messages = new NotificationMsgRequest();
        BeanUtils.copyProperties(request, messages);
        NotificationType type = EnumUtils.findEnumInsensitiveCase(NotificationType.class, request.getType());
        validateType(type);
        List<Customer> customerList = customerRepository.findAll();
        notifyService.notify(messages, customerList, type, request.getIdentifier());
        String message = messageSourceUtils.getMessage("operation.successful.send.notification");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse notifyCustomers(NotificationUsersRequest request) {
        NotificationMsgRequest messages = new NotificationMsgRequest();
        BeanUtils.copyProperties(request, messages);
        NotificationType type = EnumUtils.findEnumInsensitiveCase(NotificationType.class, request.getType());
        validateType(type);
        List<Customer> customerList = getCustomers(customerRepository, request.getCustomerIdList());
        notifyService.notify(messages, customerList, type, request.getIdentifier());
        String message = messageSourceUtils.getMessage("operation.successful.send.notification");
        return new ApiResponse(true, message);
    }

    @Transactional
    @Override
    public ApiResponse updateNotification(NotificationRequestV2 request, Integer notificationId) {
        Notification notification = RepositoryUtils.getNotification(notificationRepository, notificationId);
        NotificationType type = EnumUtils.findEnumInsensitiveCase(NotificationType.class, request.getType());
        validateType(type);
        notification.setType(type);
        notification.setIdentifier(request.getIdentifier());
        getDetails(notification, "en").setMessage(request.getEnglishMessage());
        getDetails(notification, "ar").setMessage(request.getArabicMessage());
        getDetails(notification, "fr").setMessage(request.getFrenchMessage());
        notificationRepository.save(notification);
        String message = messageSourceUtils.getMessage("operation.successful.update.notification");
        return new ApiResponse(true, message);
    }

    NotificationDetail getDetails(Notification notification, String languageCode) {
        return notification.getNotificationDetails().stream()
                .filter(i -> i.getLanguage().getCode().equals(languageCode))
                .findFirst().orElseGet(NotificationDetail::new);
    }

    private void validateType(NotificationType type) {
        if (type.equals(NotificationType.DEMANDS) || type.equals(NotificationType.ORDERS)) {
            throw new CustomException("Unsupported type %s".formatted(type.name()));
        }
    }
}
