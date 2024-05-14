package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

@Data
public class ReportResponse {
    private Integer reportId;
    private Integer reviewId;
    private Integer productId;
    private Integer reportedCustomerId;
    private Integer customerId;
    private String reportType;
    private String reportDate;
}