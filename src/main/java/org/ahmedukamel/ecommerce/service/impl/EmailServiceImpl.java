package org.ahmedukamel.ecommerce.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.EmailSenderRequest;
import org.ahmedukamel.ecommerce.dto.request.PasswordChangeEmailRequest;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.Token;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.service.EmailService;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.ahmedukamel.ecommerce.util.TokenConstants.*;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;

    @Override
    public void sendActivation(Token token) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, token.getUserId());
        sendRequest(token, customer, VITA_ACTIVATE_ACCOUNT_URL, ACTIVATE_ACCOUNT_URL);
    }

    @Override
    public void sendForgetPassword(Token token) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, token.getUserId());
        sendRequest(token, customer, VITA_FORGET_PASSWORD_URL, FORGET_PASSWORD_URL);
    }

    @Override
    public void sendPasswordChanged(Token token) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, token.getUserId());
        PasswordChangeEmailRequest request = new PasswordChangeEmailRequest();
        request.setReceivers(new String[]{customer.getEmail()});
        request.setChanged(getFormattedDate(new Date()));
        request.setRequested(getFormattedDate(token.getCreation()));
        String requestBody = new Gson().toJson(request);

        try {
            URL url = new URL(VITA_PASSWORD_CHANGED_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(requestBody);
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpStatus.CREATED.value()) {
                throw new RuntimeException();
            }
        } catch (Exception exception) {
            String message = messageSourceUtils.getMessage("operation.failed.send.email");
            throw new CustomException(message);
        }
    }

    public void sendRequest(Token token, Customer customer, String apiUrl, String link) {
        EmailSenderRequest request = new EmailSenderRequest();
        request.setReceivers(new String[]{customer.getEmail()});
        request.setLink(BASE_URL + link + token.getId());
        request.setCode(token.getCode());
        request.setExpiration(getFormattedDate(token.getExpiration()));
        String requestBody = new Gson().toJson(request);

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(requestBody);
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpStatus.CREATED.value()) {
                throw new RuntimeException();
            }
        } catch (Exception exception) {
            String message = messageSourceUtils.getMessage("operation.failed.send.email");
            throw new CustomException(message);
        }
    }

    private static String getFormattedDate(Date date) {
        return new SimpleDateFormat("E, d MMM yyyy HH:mm").format(date);
    }
}
