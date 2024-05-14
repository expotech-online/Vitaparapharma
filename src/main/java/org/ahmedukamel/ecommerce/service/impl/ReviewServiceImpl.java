package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.ReviewDto;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.ReviewMapper;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.model.Review;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.repository.ReviewRepository;
import org.ahmedukamel.ecommerce.service.ReviewService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ApiResponse addReview(ReviewDto request) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Product product = RepositoryUtils.getProduct(productRepository, request.getProductId());
        // Validating
        RepositoryService.checkNotExistReview(reviewRepository, customer.getCustomerId(), product.getProductId());
        // Processing
        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(product);
        UpdateUtils.updateReview(request, review);
        customer.getReviews().add(review);
        customerRepository.save(customer);
        UpdateUtils.updateProductRate(product);
        productRepository.save(product);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.review");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updateReview(ReviewDto request, Integer reviewId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Validating
        Review review = ValidationUtils.validateGetCustomerReview(customer, reviewId, messageSourceUtils);
        // Processing
        UpdateUtils.updateReview(request, review);
        customerRepository.save(customer);
        UpdateUtils.updateProductRate(review.getProduct());
        productRepository.save(review.getProduct());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.review");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse deleteReview(Integer reviewId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Review review = ValidationUtils.validateGetCustomerReview(customer, reviewId, messageSourceUtils);
        // Processing
        Product product = review.getProduct();
        product.getReviews().remove(review);
        customer.getReviews().remove(review);
        customerRepository.save(customer);
        UpdateUtils.updateProductRate(product);
        productRepository.save(product);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.review");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getUserReviews() {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        return getReviewsResponse(customer.getReviews());
    }

    @Override
    public ApiResponse getReview(Integer reviewId) {
        // Querying
        Review review = RepositoryUtils.getReview(reviewRepository, reviewId);
        // Processing
        ReviewDto response = ReviewMapper.toResponse(review);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.review");
        return new ApiResponse(true, message, Map.of("review", response));
    }

    @Override
    public ApiResponse getAllReviews() {
        return getReviewsResponse(reviewRepository.findAll());
    }

    @Override
    public ApiResponse getProductReviews(Integer productId) {
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        return getReviewsResponse(product.getReviews());
    }

    private ApiResponse getReviewsResponse(Collection<Review> reviews) {
        // Processing
        List<ReviewDto> reviewDtoList = ReviewMapper.toResponse(reviews);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.reviews");
        return new ApiResponse(true, message, Map.of("reviews", reviewDtoList));
    }
}