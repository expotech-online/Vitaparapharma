package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.ReviewDto;
import org.ahmedukamel.ecommerce.model.Review;

import java.util.Collection;
import java.util.List;

public class ReviewMapper {
    public static ReviewDto toResponse(Review review) {
        ReviewDto response = new ReviewDto();
        response.setReviewId(review.getReviewId());
        response.setProductId(review.getProduct().getProductId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setEmail(review.getCustomer().getEmail());
        response.setDataCreated(review.getDateCreated().toString());
        return response;
    }

    public static List<ReviewDto> toResponse(Collection<Review> reviews) {
        return reviews.stream().map(ReviewMapper::toResponse).toList();
    }
}
