package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.repository.ReviewRepository;
import org.ahmedukamel.ecommerce.validation.annotation.ValidReview;

@RequiredArgsConstructor
public class ReviewValidator implements ConstraintValidator<ValidReview, Integer> {
    private final ReviewRepository reviewRepository;

    @Override
    public boolean isValid(Integer reviewId, ConstraintValidatorContext context) {
        return reviewId != null && reviewRepository.existsById(reviewId);
    }
}
