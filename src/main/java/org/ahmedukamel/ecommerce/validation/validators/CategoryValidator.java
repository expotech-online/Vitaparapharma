package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.repository.CategoryRepository;
import org.ahmedukamel.ecommerce.validation.annotation.ValidCategory;

@RequiredArgsConstructor
public class CategoryValidator implements ConstraintValidator<ValidCategory, Integer> {
    private final CategoryRepository categoryRepository;

    @Override
    public boolean isValid(Integer categoryId, ConstraintValidatorContext constraintValidatorContext) {
        return categoryId != null && categoryRepository.existsById(categoryId);
    }
}
