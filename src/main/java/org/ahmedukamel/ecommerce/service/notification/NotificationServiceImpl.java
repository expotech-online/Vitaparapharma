package org.ahmedukamel.ecommerce.service.notification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.NotificationRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.NotificationRepository;
import org.ahmedukamel.ecommerce.service.notification.NotificationService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final CustomerRepository customerRepository;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final NotificationRepository notificationRepository;

    @Override
    public ApiResponse notifyCustomers(NotificationRequest request) {
        // Querying
        List<Customer> customerList = RepositoryUtils.getCustomers(customerRepository, request.getCustomerIdList());
        notify(customerList, request.getMessage(), languageRepository, notificationRepository);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.send.notification");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse notifyAllCustomers(String notificationMessage) {
        // Validating
        ValidationUtils.validateNotificationMessage(notificationMessage, messageSourceUtils);
        // Querying
        List<Customer> customerList = customerRepository.findAll();
        notify(customerList, notificationMessage, languageRepository, notificationRepository);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.send.notification");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updateNotification(Integer notificationId, String notificationMessage) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Notification notification = RepositoryUtils.getNotification(notificationRepository, notificationId);
        // Processing
        NotificationDetail notificationDetail = EntityDetailsUtils.supplyNotificationDetail(notification, language.getLanguageId());
        UpdateUtils.updateNotificationMessage(notificationMessage, notificationDetail);
        notificationRepository.save(notification);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.notification");
        return new ApiResponse(true, message);
    }

    public static void notify(Collection<Customer> customers, String notificationMessage, LanguageRepository languageRepository, NotificationRepository notificationRepository) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        Notification notification = new Notification();
        NotificationDetail notificationDetail = new NotificationDetail();
        notificationDetail.setLanguage(language);
        notificationDetail.setMessage(notificationMessage);
        notificationDetail.setNotification(notification);
        Set<CustomerNotification> customerNotificationList = customers.stream().map(customer -> {
            CustomerNotification customerNotification = new CustomerNotification();
            customerNotification.setCustomer(customer);
            customerNotification.setNotification(notification);
            return customerNotification;
        }).collect(Collectors.toSet());
        notification.setCustomers(customerNotificationList);
        notification.getNotificationDetails().add(notificationDetail);
        notificationRepository.save(notification);
    }
}