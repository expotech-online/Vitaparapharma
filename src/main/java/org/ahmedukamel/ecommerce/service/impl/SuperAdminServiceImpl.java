package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.RoleRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.Role;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.service.SuperAdminService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {
    private final LocalizedEnumUtils localizedEnumUtils;
    private final CustomerRepository customerRepository;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;

    @Override
    public ApiResponse setCustomerRole(RoleRequest request) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, request.getCustomerId());
        // Processing
        customer.setRole(Role.valueOf(request.getRole().toUpperCase()));
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.set.user.role");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getRoles() {
        // Processing
        Map<String, String> roles = Arrays.stream(Role.values()).collect(Collectors.toMap(Role::name, localizedEnumUtils::getRole));
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.roles");
        return new ApiResponse(true, message, Map.of("roles", roles));
    }

    @Override
    public ApiResponse setCustomerDisable(Integer customerId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, customerId);
        // Validating
        ValidationUtils.validateNonLockedCustomer(customer, messageSourceUtils);
        // Processing
        customer.setAccountNonLocked(false);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.disable.user");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse setCustomerEnable(Integer customerId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, customerId);
        // Validating
        ValidationUtils.validateLockedCustomer(customer, messageSourceUtils);
        // Processing
        customer.setAccountNonLocked(true);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.enable.user");
        return new ApiResponse(true, message);
    }
}
