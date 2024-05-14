package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.validation.annotation.ValidProduct;

@RequiredArgsConstructor
public class ProductValidator implements ConstraintValidator<ValidProduct, Integer> {
    private final ProductRepository productRepository;

    @Override
    public boolean isValid(Integer productId, ConstraintValidatorContext context) {
        return productId != null && productRepository.existsById(productId);
    }
}
