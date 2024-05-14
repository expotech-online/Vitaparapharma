package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;

    @SneakyThrows
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constrains.ValidEnum.message}" + enumClass.getSimpleName()).addConstraintViolation();
        return StringUtils.hasLength(value) && Arrays.stream(enumClass.getEnumConstants()).anyMatch(i -> i.name().equals(value.strip().toUpperCase()));
    }
}
