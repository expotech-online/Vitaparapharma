package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ahmedukamel.ecommerce.model.enumeration.ReportType;
import org.ahmedukamel.ecommerce.validation.annotation.ValidReportType;

public class ReportTypeValidator implements ConstraintValidator<ValidReportType, String> {
    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        try {
            ReportType.valueOf(type.toUpperCase());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
