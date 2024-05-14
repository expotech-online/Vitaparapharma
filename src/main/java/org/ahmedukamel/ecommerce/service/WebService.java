package org.ahmedukamel.ecommerce.service;

import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

public interface WebService {
    ModelAndView activateAccount(UUID tokenId);

    ModelAndView getChangePasswordForm(UUID tokenId);

    ModelAndView changePassword(UUID tokenId, String password);
}
