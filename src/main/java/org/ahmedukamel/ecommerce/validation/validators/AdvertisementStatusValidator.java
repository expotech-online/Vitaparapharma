package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ahmedukamel.ecommerce.model.enumeration.AdvertisementStatus;
import org.ahmedukamel.ecommerce.validation.annotation.ValidAdvertisementStatus;

public class AdvertisementStatusValidator implements ConstraintValidator<ValidAdvertisementStatus, String> {
    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        try {
            AdvertisementStatus.valueOf(status.toUpperCase());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
