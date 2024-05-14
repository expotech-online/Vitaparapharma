package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.CustomerDto;
import org.ahmedukamel.ecommerce.dto.CustomerDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.CustomerMapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.repository.*;
import org.ahmedukamel.ecommerce.service.AdminService;
import org.ahmedukamel.ecommerce.service.notification.NotificationServiceImpl;
import org.ahmedukamel.ecommerce.util.LocaleContextUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.UpdateUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final NotificationRepository notificationRepository;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ApiResponse deleteReview(Integer reviewId) {
        // Querying
        Review review = RepositoryUtils.getReview(reviewRepository, reviewId);
        // Processing
        Product product = review.getProduct();
        product.getReviews().remove(review);
        UpdateUtils.updateProductRate(product);
        productRepository.save(product);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.review");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse resetDemands(Integer productId) {
        // Querying
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        // Processing
        product.getDemands().clear();
        productRepository.save(product);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.demands");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse resetDemandsAndNotify(Integer productId, String notificationMessage) {
        // Querying
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        // Processing
        List<Customer> customerList = product.getDemands().stream().map(Demand::getCustomer).toList();
        NotificationServiceImpl.notify(customerList, notificationMessage, languageRepository, notificationRepository);
        product.getDemands().clear();
        productRepository.save(product);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.notify.demands");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getAllCustomers() {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDtoV2> customerDtoList = CustomerMapper.toResponseV2(customers, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.users");
        return new ApiResponse(true, message, Map.of("customers", customerDtoList));
    }

    @Override
    public ApiResponse getCustomer(Integer customerId) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Customer customer = RepositoryUtils.getCustomer(customerRepository, customerId);
        // Processing
        CustomerDtoV2 response = CustomerMapper.toResponseV2(customer, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.user");
        return new ApiResponse(true, message, Map.of("customer", response));
    }

}