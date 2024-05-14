package org.ahmedukamel.ecommerce.service;

import org.ahmedukamel.ecommerce.dto.request.EmailCodeRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

public interface AccountActivationService {
    ApiResponse resendActivationCode(EmailRequest request);

    ApiResponse activateAccount(EmailCodeRequest request);
}
