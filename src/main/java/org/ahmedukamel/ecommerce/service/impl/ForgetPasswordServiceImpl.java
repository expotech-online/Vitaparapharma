package org.ahmedukamel.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.ChangePasswordRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailCodeRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.model.Token;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.TokenRepository;
import org.ahmedukamel.ecommerce.service.EmailService;
import org.ahmedukamel.ecommerce.service.ForgetPasswordService;
import org.ahmedukamel.ecommerce.service.TokenService;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.ValidationUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class ForgetPasswordServiceImpl implements ForgetPasswordService {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Override
    public ApiResponse sendForgetPasswordCode(EmailRequest request) {
        RepositoryService.checkExistEmail(customerRepository, request.getEmail().strip());
        CompletableFuture<Token> tokenFuture = CompletableFuture.supplyAsync(() -> tokenService.createForgetPasswordToken(request), executor);
        tokenFuture.thenAcceptAsync(emailService::sendForgetPassword, executor);
//        String message = messageSourceUtils.getMessage("");
        return new ApiResponse(true, "reset password sent");
    }

    @Override
    public ApiResponse validateCode(EmailCodeRequest request) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, request.getEmail().strip(), Provider.NONE);
        Optional<Token> optionalToken = tokenRepository.findByCode(request.getCode().strip());
        ValidationUtils.validateCustomerToken(optionalToken, customer.getCustomerId(), messageSourceUtils);
        ValidationUtils.validateForgetPasswordToken(optionalToken.orElseGet(Token::new), messageSourceUtils);
//        String message = messageSourceUtils.getMessage("");
        return new ApiResponse(true, "valid code");
    }

    @Override
    public ApiResponse changePassword(ChangePasswordRequest request) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, request.getEmail().strip(), Provider.NONE);
        Optional<Token> optionalToken = tokenRepository.findByCode(request.getCode().strip());
        ValidationUtils.validateCustomerToken(optionalToken, customer.getCustomerId(), messageSourceUtils);
        Token token = optionalToken.orElseGet(Token::new);
        ValidationUtils.validateForgetPasswordToken(token, messageSourceUtils);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customerRepository.save(customer);
        token.setUsed(true);
        tokenRepository.save(token);
        CompletableFuture.runAsync(() -> emailService.sendPasswordChanged(token), executor);
//        String message = messageSourceUtils.getMessage("");
        return new ApiResponse(true, "changed successfully");
    }
}
