package org.ahmedukamel.ecommerce.service;

import org.ahmedukamel.ecommerce.dto.request.EmailRequest;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.Token;

public interface TokenService {
    Token createActivateAccountToken(Customer customer);

    Token createForgetPasswordToken(EmailRequest request);
}