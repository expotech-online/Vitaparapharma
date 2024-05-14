package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.ReportRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.ReportResponse;
import org.ahmedukamel.ecommerce.mapper.ReportMapper;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.ReportType;
import org.ahmedukamel.ecommerce.model.Review;
import org.ahmedukamel.ecommerce.model.ReviewReport;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.ReviewRepository;
import org.ahmedukamel.ecommerce.service.CustomerReportService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerReportServiceImpl implements CustomerReportService {
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final LocalizedEnumUtils localizedEnumUtils;
    private final ReviewRepository reviewRepository;

    @Override
    public ApiResponse reportReview(ReportRequest request) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Review review = RepositoryUtils.getReview(reviewRepository, request.getReviewId());
        // Validating
        ValidationUtils.validCustomerReport(customer, review.getReviewId(), messageSourceUtils);
        ReportType type = ReportType.valueOf(request.getType().toUpperCase());
        // Processing
        ReviewReport report = new ReviewReport();
        report.setCustomer(customer);
        report.setReview(review);
        report.setType(type);
        customer.getReports().add(report);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.review.report");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getReports() {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        List<ReportResponse> reportResponseList = ReportMapper.toResponse(customer.getReports());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.review.reports");
        return new ApiResponse(true, message, Map.of("reports", reportResponseList));
    }

    @Override
    public ApiResponse getReportTypes() {
        // Processing
        Map<String, String> reportTypeMap = Arrays.stream(ReportType.values()).collect(Collectors.toMap(ReportType::name, localizedEnumUtils::getReportType));
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.review.report.types");
        return new ApiResponse(true, message, Map.of("types", reportTypeMap));
    }
}
