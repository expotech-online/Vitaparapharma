package org.ahmedukamel.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.model.enumeration.Role;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.util.JwtUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final MessageSourceUtils messageSourceUtils;
    private final CustomerRepository customerRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2Token) {
            Provider provider = Provider.valueOf(oAuth2Token.getAuthorizedClientRegistrationId().toUpperCase());
            OAuth2User oAuth2User = oAuth2Token.getPrincipal();
            String providerId;
            if (provider.equals(Provider.FACEBOOK)) {
                providerId = oAuth2User.getAttribute("id");
            } else if (provider.equals(Provider.GOOGLE)) {
                providerId = oAuth2User.getAttribute(StandardClaimNames.SUB);
            } else {
                throw new CustomException("Unknown Authentication");
            }
            Optional<Customer> customerOpt = customerRepository.findByProviderAndProviderId(provider, providerId);
            String email = oAuth2User.getAttribute(StandardClaimNames.EMAIL);
            Customer customer;
            if (customerOpt.isPresent()) {
                customer = customerOpt.get();
            } else {
                customer = new Customer();
                Cart cart = new Cart();
                Wishlist wishlist = new Wishlist();
                customer.setProvider(provider);
                customer.setRole(Role.CUSTOMER);
                customer.setProviderId(providerId);
                customer.setEnabled(true);
                customer.setPhone(null);
                customer.setCart(cart);
                customer.setWishlist(wishlist);
                cart.setCustomer(customer);
                wishlist.setCustomer(customer);
            }
            assert email != null;
            customer.setEmail(email.toLowerCase().strip());
            customer = customerRepository.save(customer);

            String message = messageSourceUtils.getMessage("operation.successful.login");
            String token = JwtUtils.generateToken(customer, false, Map.of("provider", provider.name()));
//            ApiResponse apiResponse = new ApiResponse(true, message, Map.of("token", token));
//            new ObjectMapper().writeValue(response.getWriter(), apiResponse);
            response.sendRedirect("/api/v1/auth/oauth?token=" + token);
//            response.sendRedirect("https://api.vitaparapharma.com/api/v1/auth/oauth?token=" + token);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unsupported Authentication");
        }
    }
}