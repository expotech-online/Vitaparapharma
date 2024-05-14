package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewReportRepository extends JpaRepository<ReviewReport, Integer> {
    List<ReviewReport> findAllByCustomer_CustomerId(Integer customerId);

    List<ReviewReport> findAllByReview_ReviewId(Integer reviewId);

    List<ReviewReport> findAllByReview_Product_ProductId(Integer productId);
}
