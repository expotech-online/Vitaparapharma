package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.LoginRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.request.RegistrationRequest;
import org.ahmedukamel.ecommerce.mapper.CustomerMapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.service.AuthService;
import org.ahmedukamel.ecommerce.service.EmailService;
import org.ahmedukamel.ecommerce.service.TokenService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    @Override
    public ApiResponse register(Object object) {
        RegistrationRequest request = (RegistrationRequest) object;
        // Validating
        RepositoryService.checkNotExistEmail(customerRepository, request.getEmail(), Provider.NONE);
        RepositoryService.checkNotExistPhone(customerRepository, request.getPhone());
        // Processing
        Customer customer = CustomerMapper.toCustomer(passwordEncoder, request);
        Cart cart = new Cart();
        Wishlist wishlist = new Wishlist();
        customer.setCart(cart);
        customer.setWishlist(wishlist);
        if (StringUtils.hasLength(request.getDeviceToken()))
            customer.getDeviceTokens().add(request.getDeviceToken().strip());
        cart.setCustomer(customer);
        wishlist.setCustomer(customer);
        customerRepository.save(customer);
        // Tasks to be running in thread
        CompletableFuture<Token> tokenFuture = CompletableFuture.supplyAsync(() -> tokenService.createActivateAccountToken(customer), executor);
        tokenFuture.thenAcceptAsync(emailService::sendActivation, executor);
        String token = JwtUtils.generateToken(customer, request.rememberMe, Map.of("provider", Provider.NONE.name()));
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.register");
        return new ApiResponse(true, message, Map.of("token", token));
    }

    @Override
    public ApiResponse login(Object object) {
        LoginRequest request = (LoginRequest) object;
        // Querying
        Customer customer = RepositoryUtils.getCustomerByEmailOrPhone(customerRepository, request.getEmail());
        // Processing
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customer.getEmail(), request.getPassword()));
        if (StringUtils.hasLength(request.getDeviceToken())) {
            customer.getDeviceTokens().add(request.getDeviceToken().strip());
            customerRepository.save(customer);
        }
        String token = JwtUtils.generateToken(customer, request.rememberMe, Map.of("provider", Provider.NONE.name()));
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.login");
        return new ApiResponse(true, message, Map.of("token", token));
    }
}
