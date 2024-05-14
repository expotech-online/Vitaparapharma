package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    boolean existsByCustomer_CustomerIdAndProduct_ProductId(Integer customerId, Integer productId);
}
