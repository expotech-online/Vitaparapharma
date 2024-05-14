package org.ahmedukamel.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.EmailRequest;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.model.Token;
import org.ahmedukamel.ecommerce.model.enumeration.TokenType;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.TokenRepository;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;

    private static final long ACTIVATE_ACCOUNT_DURATION = 1_000 * 60 * 60 * 24;
    private static final long FORGET_PASSWORD_DURATION = 1_000 * 60 * 10;

    @Override
    public Token createActivateAccountToken(Customer customer) {
        tokenRepository.revokeUserTokens(customer.getCustomerId(), TokenType.ACTIVATE_ACCOUNT);
        long currentTimeMillis = System.currentTimeMillis();
        Token token = new Token();
        token.setCode(generateShortCode());
        token.setUserId(customer.getCustomerId());
        token.setTokenType(TokenType.ACTIVATE_ACCOUNT);
        token.setCreation(new Date(currentTimeMillis));
        token.setExpiration(new Date(currentTimeMillis + ACTIVATE_ACCOUNT_DURATION));
        token.setUsed(false);
        token.setRevoked(false);
        return tokenRepository.save(token);
    }

    @Override
    public Token createForgetPasswordToken(EmailRequest request) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, request.getEmail().strip(), Provider.NONE);
        tokenRepository.revokeUserTokens(customer.getCustomerId(), TokenType.FORGET_PASSWORD);
        long currentTimeMillis = System.currentTimeMillis();
        Token token = new Token();
        token.setCode(generateShortCode());
        token.setUserId(customer.getCustomerId());
        token.setTokenType(TokenType.FORGET_PASSWORD);
        token.setCreation(new Date(currentTimeMillis));
        token.setExpiration(new Date(currentTimeMillis + FORGET_PASSWORD_DURATION));
        token.setUsed(false);
        token.setRevoked(false);
        return tokenRepository.save(token);
    }

    public String generateShortCode() {
        String code = null;
        while (code == null || tokenRepository.existsByCode(code)) {
            int number = new Random().nextInt(999_999);
            code = String.format("%06d", number);
        }
        return code;
    }
}
