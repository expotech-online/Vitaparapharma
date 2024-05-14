package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByProviderAndProviderId(Provider provider, String providerId);

    Optional<Customer> findByEmailIgnoreCaseAndProvider(String email, Provider provider);

    Optional<Customer> findByEmailIgnoreCaseAndProviderOrPhone(String email, Provider provider, String phone);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndProvider(String email, Provider provider);

    boolean existsByPhone(String phone);

    List<Customer> findAllByEnabled(boolean enabled);
}