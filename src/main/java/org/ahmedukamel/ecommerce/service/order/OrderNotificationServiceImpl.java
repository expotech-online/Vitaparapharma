package org.ahmedukamel.ecommerce.service.order;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.ahmedukamel.ecommerce.model.enumeration.OrderStatus;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.NotificationRepository;
import org.ahmedukamel.ecommerce.util.LocalizedEnumUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OrderNotificationServiceImpl implements OrderNotificationService {
    private final NotificationRepository notificationRepository;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final LocalizedEnumUtils localizedEnumUtils;

    @Override
    public void notify(Order order, OrderStatus status) {
        Customer customer = order.getCustomer();
        Notification notification = new Notification();
        CustomerNotification customerNotification = new CustomerNotification();
        customerNotification.setCustomer(customer);
        customerNotification.setNotification(notification);
        notification.getCustomers().add(customerNotification);
        notification.setType(NotificationType.ORDERS);
        notification.setIdentifier(order.getOrderId().toString());

        languageRepository.findAll().forEach(language -> {
            String code = "notification.message.order.status";
            Locale locale = new Locale(language.getCode());
            String message = messageSourceUtils.getMessageByLocale(code, locale,
                    order.getOrderId(), localizedEnumUtils.getOrderStatus(status.name(), locale));
            NotificationDetail notificationDetail = new NotificationDetail();
            notificationDetail.setNotification(notification);
            notificationDetail.setMessage(message);
            notificationDetail.setLanguage(language);
            notification.getNotificationDetails().add(notificationDetail);
        });

        customer.getNotifications().add(customerNotification);
        notificationRepository.save(notification);
    }
}
