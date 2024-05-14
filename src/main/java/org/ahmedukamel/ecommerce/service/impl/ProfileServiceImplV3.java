package org.ahmedukamel.ecommerce.service.impl;

import org.ahmedukamel.ecommerce.dto.CustomerDto;
import org.ahmedukamel.ecommerce.dto.CustomerDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.exception.EntityNotFoundException;
import org.ahmedukamel.ecommerce.mapper.CustomerMapper;
import org.ahmedukamel.ecommerce.mapper.NotificationMapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.repository.CustomerNotificationRepository;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.util.*;
import org.ahmedukamel.ecommerce.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
public class ProfileServiceImplV3 extends ProfileServiceImpl {
    final LocalizedEnumUtils localizedEnumUtils;
    final CustomerNotificationRepository customerNotificationRepository;

    public ProfileServiceImplV3(PasswordEncoder passwordEncoder, CustomerRepository customerRepository, LanguageRepository languageRepository, MessageSourceUtils messageSourceUtils, LocalizedEnumUtils localizedEnumUtils, CustomerNotificationRepository customerNotificationRepository) {
        super(passwordEncoder, customerRepository, languageRepository, messageSourceUtils);
        this.localizedEnumUtils = localizedEnumUtils;
        this.customerNotificationRepository = customerNotificationRepository;
    }

    @Override
    public ApiResponse readNotification(Integer notificationId) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Set<CustomerNotification> cnList = customer.getNotifications();
        Optional<CustomerNotification> cnOptional = cnList.stream().filter(i -> i.getId().equals(notificationId)).findFirst();
        if (cnOptional.isEmpty()) {
            throw new EntityNotFoundException(notificationId.toString(), Notification.class);
        }
        cnOptional.get().setRead(true);
        customerRepository.save(customer);
        String message = "success";
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getUnreadNotifications() {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        long unread = customer.getNotifications().stream().filter(i -> !i.isRead()).count();
        return new ApiResponse(true, "unread", Map.of("unread", unread));
    }

    @Override
    Object getResponseData(Customer customer, Language language) {
        Set<CustomerNotification> notificationSet = customer.getNotifications();
        return NotificationMapper.toResponseV3(notificationSet, language.getLanguageId(), localizedEnumUtils);
    }

    @Override
    public ApiResponse updateUser(CustomerDto object) {
        CustomerDtoV2 request = (CustomerDtoV2) object;
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        CustomerDetail customerDetail = EntityDetailsUtils.supplyCustomerDetail(customer, language.getLanguageId());
        customerDetail.setCustomer(customer);
        customerDetail.setLanguage(language);
        if (Validator.NOT_NULL_NOT_BLANK(request.getFirstName())) {
            customerDetail.setFirstName(request.getFirstName().strip());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getLastName())) {
            customerDetail.setLastName(request.getLastName().strip());
        }
        if (Validator.EMAIL(request.getEmail())) {
            customer.setEmail(request.getEmail().toLowerCase().strip());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getPhone())) {
            customer.setPhone(request.getPhone().strip());
        }
        if (request.getDateOfBirth() != null) {
            try {
                customer.setDateOfBirth(LocalDate.parse(request.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } catch (Exception ignore) {}
        }
        customer.setMale(request.isMale());
        customer.getCustomerDetails().add(customerDetail);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.user.profile");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getProfile() {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        CustomerDtoV2 response = CustomerMapper.toResponseV2(customer, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.user.profile");
        return new ApiResponse(true, message, Map.of("user", response));
    }
}