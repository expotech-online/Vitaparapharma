package org.ahmedukamel.ecommerce.service.notification;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.NotificationMsgRequest;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.ahmedukamel.ecommerce.repository.CustomerNotificationRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.NotificationRepository;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getLanguage;

@Service
@RequiredArgsConstructor
public class NotifyServiceV1 implements NotifyService {
    final CustomerNotificationRepository cnRepository;
    final LanguageRepository languageRepository;
    final NotificationRepository notificationRepository;
    final FirebaseNotifier firebaseNotifier;
    final MessageSourceUtils messageSourceUtils;

    @Override
    public void notify(NotificationMsgRequest messages, List<Customer> customerList, NotificationType type, String identifier) {
        Notification notification = new Notification();
        notification.setIdentifier(identifier);
        notification.setType(type);

        NotificationDetail enDetails = new NotificationDetail();
        enDetails.setMessage(messages.getEnglishMessage());
        enDetails.setLanguage(getLanguage(languageRepository, "en"));
        enDetails.setNotification(notification);

        NotificationDetail arDetails = new NotificationDetail();
        arDetails.setMessage(messages.getArabicMessage());
        arDetails.setLanguage(getLanguage(languageRepository, "ar"));
        arDetails.setNotification(notification);

        NotificationDetail frDetails = new NotificationDetail();
        frDetails.setMessage(messages.getFrenchMessage());
        frDetails.setLanguage(getLanguage(languageRepository, "fr"));
        frDetails.setNotification(notification);

        Set<CustomerNotification> cnList = customerList.stream().map(customer -> {
            CustomerNotification cn = new CustomerNotification();
            cn.setCustomer(customer);
            cn.setNotification(notification);
            return cn;
        }).collect(Collectors.toSet());

        notification.setCustomers(cnList);
        notification.setNotificationDetails(Set.of(enDetails, arDetails, frDetails));
        notificationRepository.save(notification);

        String title = switch (type) {
            case OTHERS -> messageSourceUtils.getMessageByLocale("OTHERS", new Locale("ar")); //"General Notification."
            case ORDERS -> messageSourceUtils.getMessageByLocale("ORDERS", new Locale("ar")); // "Check out your order."
            case COUPONS -> messageSourceUtils.getMessageByLocale("COUPONS", new Locale("ar")); //"Check out our new COUPON !!"
            case OFFERS -> messageSourceUtils.getMessageByLocale("OFFERS", new Locale("ar")); // "We have a new offer on our product."
            case DEMANDS -> messageSourceUtils.getMessageByLocale("DEMANDS", new Locale("ar")); // "The product you have been waiting for is here !"
        };

        customerList.forEach(customer -> customer.getDeviceTokens().forEach(dt -> firebaseNotifier.send(title, arDetails.getMessage(), dt)));
    }
}
