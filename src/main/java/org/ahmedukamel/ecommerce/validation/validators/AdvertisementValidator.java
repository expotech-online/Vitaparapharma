package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.repository.AdvertisementRepository;
import org.ahmedukamel.ecommerce.validation.annotation.ValidAdvertisement;

@RequiredArgsConstructor
public class AdvertisementValidator implements ConstraintValidator<ValidAdvertisement, Integer> {
    private final AdvertisementRepository advertisementRepository;

    @Override
    public boolean isValid(Integer advertisementId, ConstraintValidatorContext context) {
        return advertisementId != null && advertisementRepository.existsById(advertisementId);
    }
}
