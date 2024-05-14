package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.CustomerDto;
import org.ahmedukamel.ecommerce.dto.request.UpdatePasswordRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.CustomerMapper;
import org.ahmedukamel.ecommerce.mapper.NotificationMapper;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.CustomerDetail;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.service.ProfileService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {
    final PasswordEncoder passwordEncoder;
    final CustomerRepository customerRepository;
    final LanguageRepository languageRepository;
    final MessageSourceUtils messageSourceUtils;

    @Override
    public ApiResponse updateUser(CustomerDto request) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        CustomerDetail customerDetail = EntityDetailsUtils.supplyCustomerDetail(customer, language.getLanguageId());
        customerDetail.setCustomer(customer);
        customerDetail.setLanguage(language);
        UpdateUtils.validateUpdateCustomer(customerRepository, request, customer);
        UpdateUtils.updateCustomerDetail(request, customerDetail);
        customer.getCustomerDetails().add(customerDetail);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.user.profile");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updatePassword(UpdatePasswordRequest request) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Validating & Processing
        UpdateUtils.validateUpdatePassword(passwordEncoder, request, customer, messageSourceUtils);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.user.password");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getProfile() {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        CustomerDto user = CustomerMapper.toResponse(customer, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.user.profile");
        return new ApiResponse(true, message, Map.of("user", user));
    }

    @Override
    public ApiResponse getNotifications() {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        Object notificationResponseList = getResponseData(customer, language);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.notifications");
        return new ApiResponse(true, message, Map.of("notifications", notificationResponseList));
    }

    @Override
    public ApiResponse getUnreadNotifications() {
        return null;
    }

    @Override
    public ApiResponse readNotification(Integer notificationId) {
        return null;
    }

    Object getResponseData(Customer customer, Language language) {
        return NotificationMapper.toResponse(customer.getNotifications(), language.getLanguageId());
    }
}