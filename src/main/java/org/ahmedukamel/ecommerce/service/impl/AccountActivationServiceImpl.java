package org.ahmedukamel.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.EmailCodeRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.model.Token;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.TokenRepository;
import org.ahmedukamel.ecommerce.service.AccountActivationService;
import org.ahmedukamel.ecommerce.service.EmailService;
import org.ahmedukamel.ecommerce.service.TokenService;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class AccountActivationServiceImpl implements AccountActivationService {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Override
    public ApiResponse resendActivationCode(EmailRequest request) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, request.getEmail().strip(), Provider.NONE);
        if (!customer.isEnabled()) {
            CompletableFuture<Token> tokenFuture = CompletableFuture.supplyAsync(() -> tokenService.createActivateAccountToken(customer), executor);
            tokenFuture.thenAcceptAsync(emailService::sendActivation, executor);
        }
        String message = messageSourceUtils.getMessage("operation.successful.resend.activation.code");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse activateAccount(EmailCodeRequest request) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, request.getEmail().strip(), Provider.NONE);
        Optional<Token> optionalToken = tokenRepository.findByCode(request.getCode());
        ValidationUtils.validateCustomerToken(optionalToken, customer.getCustomerId(), messageSourceUtils);
        ValidationUtils.validateActivateAccountToken(optionalToken.orElseGet(Token::new), messageSourceUtils);
        Token token = optionalToken.orElseGet(Token::new);
        token.setUsed(true);
        customer.setEnabled(true);
        customerRepository.save(customer);
        tokenRepository.save(token);
        String message = messageSourceUtils.getMessage("operation.successful.account.activation");
        return new ApiResponse(true, message);
    }
}
