package org.ahmedukamel.ecommerce.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.validation.annotation.ValidCustomerList;

import java.util.Collection;

@RequiredArgsConstructor
public class CustomerListValidator implements ConstraintValidator<ValidCustomerList, Collection<Integer>> {
    private final CustomerRepository customerRepository;

    @Override
    public boolean isValid(Collection<Integer> customerIds, ConstraintValidatorContext context) {
        return customerIds != null && customerIds.stream().anyMatch(customerRepository::existsById);
    }
}
