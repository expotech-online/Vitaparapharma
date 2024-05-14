package org.ahmedukamel.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.ChangePasswordRequest;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.exception.EntityNotFoundException;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.Token;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.TokenRepository;
import org.ahmedukamel.ecommerce.service.EmailService;
import org.ahmedukamel.ecommerce.service.WebService;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.ValidationUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class WebServiceImpl implements WebService {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public ModelAndView activateAccount(UUID tokenId) {
        return supplier(() -> {
            Token token = RepositoryUtils.getToken(tokenId, tokenRepository);
            ValidationUtils.validateActivateAccountToken(token, messageSourceUtils);

            Customer customer = RepositoryUtils.getCustomer(customerRepository, token.getUserId());
            customer.setEnabled(true);
            customerRepository.save(customer);

            token.setUsed(true);
            tokenRepository.save(token);

            return new ModelAndView("success", Map.of("title", "Account Activation",
                    "heading", "Success",
                    "message", "Account have been activated successfully!"));
        });
    }

    @Override
    public ModelAndView getChangePasswordForm(UUID tokenId) {
        return supplier(() -> {
            Token token = RepositoryUtils.getToken(tokenId, tokenRepository);
            ValidationUtils.validateForgetPasswordToken(token, messageSourceUtils);
            return new ModelAndView("change-password", Map.of("tokenIn", tokenId));
        });
    }

    @Override
    public ModelAndView changePassword(UUID tokenId, String password) {
        return supplier(() -> {
            Token token = RepositoryUtils.getToken(tokenId, tokenRepository);
            ValidationUtils.validateForgetPasswordToken(token, messageSourceUtils);

            assert password != null;
            Customer customer = RepositoryUtils.getCustomer(customerRepository, token.getUserId());
            customer.setPassword(passwordEncoder.encode(password));
            customerRepository.save(customer);

            token.setUsed(true);
            tokenRepository.save(token);

            CompletableFuture.runAsync(() -> emailService.sendPasswordChanged(token), executor);

            return new ModelAndView("success", Map.of("title", "Change Password",
                    "heading", "Success",
                    "message", "Password have been changed successfully!"));
        });
    }

    private ModelAndView supplier(Supplier<ModelAndView> supplier) {
        try {
            return supplier.get();
        } catch (CustomException exception) {
            return new ModelAndView("error", Map.of("title", "Account Activation",
                    "heading", "Failed",
                    "message", exception.getMessage()));
        } catch (EntityNotFoundException exception) {
            System.out.println(exception.getMessage());
            return new ModelAndView("error", Map.of("title", "Account Activation",
                    "heading", "Failed",
                    "message", "Invalid Token"));
        }
    }
}
