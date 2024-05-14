package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.ChangePasswordRequest;
import org.ahmedukamel.ecommerce.service.WebService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "p")
public class WebController {
    private final WebService service;

    @GetMapping(value = "activate-account/{tokenId}")
    public ModelAndView activateAccount(@PathVariable(value = "tokenId") UUID tokenId) {
        return service.activateAccount(tokenId);
    }

    @GetMapping(value = "change-password/{tokenId}")
    public ModelAndView getChangePasswordForm(@PathVariable(value = "tokenId") UUID tokenId) {
        return service.getChangePasswordForm(tokenId);
    }

    @PostMapping(value = "change-password/{tokenId}")
    public ModelAndView changePassword(@PathVariable(value = "tokenId") UUID tokenId, @RequestParam(value = "password") String password) {
        System.out.println(password);
        return service.changePassword(tokenId, password);
    }
}
