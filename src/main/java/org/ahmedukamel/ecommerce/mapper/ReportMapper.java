package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.response.ReportResponse;
import org.ahmedukamel.ecommerce.model.ReviewReport;

import java.util.Collection;
import java.util.List;

public class ReportMapper {
    public static ReportResponse toResponse(ReviewReport report) {
        ReportResponse response = new ReportResponse();
        response.setReportId(report.getReportId());
        response.setReviewId(report.getReview().getReviewId());
        response.setProductId(report.getReview().getProduct().getProductId());
        response.setReportedCustomerId(report.getReview().getCustomer().getCustomerId());
        response.setCustomerId(report.getCustomer().getCustomerId());
        response.setReportType(report.getType().name());
        response.setReportDate(report.getDateTime().toString());
        return response;
    }

    public static List<ReportResponse> toResponse(Collection<ReviewReport> item) {
        return item.stream().map(ReportMapper::toResponse).toList();
    }
}
