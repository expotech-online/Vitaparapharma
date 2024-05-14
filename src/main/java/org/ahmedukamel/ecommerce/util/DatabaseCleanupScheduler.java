package org.ahmedukamel.ecommerce.util;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.TokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Transactional
public class DatabaseCleanupScheduler {
    private final TokenRepository tokenRepository;
    private final CustomerRepository customerRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredOrRevokedTokens() {
        tokenRepository.deleteTokens(new Date());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteDisabledUsers() {
        customerRepository.findAllByEnabled(false).forEach(customer -> {
            tokenRepository.deleteAllByUserId(customer.getCustomerId());
            customerRepository.delete(customer);
        });
    }
}
