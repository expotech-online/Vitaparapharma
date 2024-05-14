package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.repository.MainCategoryRepository;
import org.ahmedukamel.ecommerce.validation.annotation.ValidMainCategory;

@RequiredArgsConstructor
public class MainCategoryValidator implements ConstraintValidator<ValidMainCategory, Integer> {
    private final MainCategoryRepository mainCategoryRepository;

    @Override
    public boolean isValid(Integer categoryId, ConstraintValidatorContext constraintValidatorContext) {
        return categoryId != null && mainCategoryRepository.existsById(categoryId);
    }
}
