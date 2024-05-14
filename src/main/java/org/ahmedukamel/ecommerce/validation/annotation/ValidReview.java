package org.ahmedukamel.ecommerce.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.ahmedukamel.ecommerce.validation.validators.ReviewValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReviewValidator.class)
public @interface ValidReview {
    String message() default "{validation.constrains.ValidReview.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
