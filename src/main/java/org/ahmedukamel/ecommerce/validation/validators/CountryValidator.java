package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ahmedukamel.ecommerce.model.enumeration.Country;
import org.ahmedukamel.ecommerce.validation.annotation.ValidCountry;

public class CountryValidator implements ConstraintValidator<ValidCountry, String> {
    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        try {
            Country.valueOf(country.toUpperCase());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
