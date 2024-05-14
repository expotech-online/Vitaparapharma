package org.ahmedukamel.ecommerce.service;

import org.ahmedukamel.ecommerce.dto.request.ChangePasswordRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailCodeRequest;
import org.ahmedukamel.ecommerce.dto.request.EmailRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

public interface ForgetPasswordService {
    ApiResponse sendForgetPasswordCode(EmailRequest request);

    ApiResponse validateCode(EmailCodeRequest request);

    ApiResponse changePassword(ChangePasswordRequest request);
}
