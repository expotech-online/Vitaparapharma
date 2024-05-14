package org.ahmedukamel.ecommerce.dto.request;

import lombok.Data;
import org.ahmedukamel.ecommerce.validation.annotation.ValidReportType;
import org.ahmedukamel.ecommerce.validation.annotation.ValidReview;

@Data
public class ReportRequest {
    @ValidReportType
    private String type;
    @ValidReview
    private Integer reviewId;
}
