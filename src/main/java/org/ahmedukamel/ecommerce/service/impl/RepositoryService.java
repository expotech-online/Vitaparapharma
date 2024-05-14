package org.ahmedukamel.ecommerce.service.impl;

import org.ahmedukamel.ecommerce.exception.DuplicateEntryException;
import org.ahmedukamel.ecommerce.exception.EntityNotFoundException;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.repository.*;

public class RepositoryService {
    public static void checkExistCustomer(CustomerRepository repository, Integer customerId) {
        if (!repository.existsById(customerId)) {
            throw new EntityNotFoundException(customerId.toString(), Customer.class);
        }
    }

    public static void checkNotExistEmail(CustomerRepository repository, String email, Provider provider) {
        if (repository.existsByEmailIgnoreCaseAndProvider(email.strip(), provider)) {
            throw new DuplicateEntryException(email, Customer.class);
        }
    }

    public static void checkExistEmail(CustomerRepository repository, String email) {
        if (!repository.existsByEmailIgnoreCase(email.strip())) {
            throw new EntityNotFoundException(email, Customer.class);
        }
    }

    public static void checkNotExistPhone(CustomerRepository repository, String phone) {
        if (repository.existsByPhone(phone.strip())) {
            throw new DuplicateEntryException(phone, Customer.class);
        }
    }

    public static void checkExistProduct(ProductRepository repository, Integer productId) {
        if (!repository.existsById(productId)) {
            throw new EntityNotFoundException(productId.toString(), Product.class);
        }
    }

    public static void checkNotExistReview(ReviewRepository repository, Integer customerId, Integer productId) {
        if (repository.existsByCustomer_CustomerIdAndProduct_ProductId(customerId, productId)) {
            throw new DuplicateEntryException("customer id " + customerId, Review.class);
        }
    }

    public static void checkExistReview(ReviewRepository repository, Integer reviewId) {
        if (!repository.existsById(reviewId)) {
            throw new EntityNotFoundException(reviewId.toString(), Review.class);
        }
    }
}
