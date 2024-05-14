package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.ReportResponse;
import org.ahmedukamel.ecommerce.mapper.ReportMapper;
import org.ahmedukamel.ecommerce.model.ReviewReport;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.repository.ReviewReportRepository;
import org.ahmedukamel.ecommerce.repository.ReviewRepository;
import org.ahmedukamel.ecommerce.service.ReportService;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {
    private final ReviewReportRepository reviewReportRepository;
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ApiResponse getAllReports() {
        // Querying
        List<ReviewReport> reportList = reviewReportRepository.findAll();
        // Processing
        List<ReportResponse> reportResponseList = ReportMapper.toResponse(reportList);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.reports");
        return new ApiResponse(true, message, Map.of("reports", reportResponseList));
    }

    @Override
    public ApiResponse getProductReports(Integer productId) {
        // Querying
        RepositoryService.checkExistProduct(productRepository, productId);
        // Processing
        List<ReviewReport> reportList = reviewReportRepository.findAllByReview_Product_ProductId(productId);
        List<ReportResponse> reportResponseList = ReportMapper.toResponse(reportList);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.product.reports");
        return new ApiResponse(true, message, Map.of("reports", reportResponseList));
    }

    @Override
    public ApiResponse getReviewReports(Integer reviewId) {
        // Querying
        RepositoryService.checkExistReview(reviewRepository, reviewId);
        // Processing
        List<ReviewReport> reportList = reviewReportRepository.findAllByReview_ReviewId(reviewId);
        List<ReportResponse> reportResponseList = ReportMapper.toResponse(reportList);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.review.reports");
        return new ApiResponse(true, message, Map.of("reports", reportResponseList));
    }

    @Override
    public ApiResponse getReportsFromCustomer(Integer customerId) {
        // Querying
        RepositoryService.checkExistCustomer(customerRepository, customerId);
        // Processing
        List<ReviewReport> reportList = reviewReportRepository.findAllByCustomer_CustomerId(customerId);
        List<ReportResponse> reportResponseList = ReportMapper.toResponse(reportList);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.from.user.reports");
        return new ApiResponse(true, message, Map.of("reports", reportResponseList));
    }

    @Override
    public ApiResponse getReportsToCustomer(Integer customerId) {
        // Querying
        RepositoryService.checkExistCustomer(customerRepository, customerId);
        // Processing
        List<ReviewReport> reportList = reviewReportRepository.findAllByCustomer_CustomerId(customerId);
        List<ReportResponse> reportResponseList = ReportMapper.toResponse(reportList);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.to.user.reports");
        return new ApiResponse(true, message, Map.of("reports", reportResponseList));
    }
}