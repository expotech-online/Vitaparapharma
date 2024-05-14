package org.ahmedukamel.ecommerce.service;

import org.ahmedukamel.ecommerce.model.Token;

public interface EmailService {
    void sendActivation(Token token);

    void sendForgetPassword(Token token);

    void sendPasswordChanged(Token token);
}
