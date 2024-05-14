package org.ahmedukamel.ecommerce.config;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConfig {
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Optional<Customer> optionalCustomer = customerRepository.findByEmailIgnoreCaseAndProvider(email.strip(), Provider.NONE);
            if (optionalCustomer.isEmpty()) {
                String message = messageSourceUtils.getMessage("operation.failed.not.found.user");
                throw new CustomException(message);
            }
            return optionalCustomer.get();
        };
    }
}
