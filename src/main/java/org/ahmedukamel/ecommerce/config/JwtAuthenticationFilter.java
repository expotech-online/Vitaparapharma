package org.ahmedukamel.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.util.JwtUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    public static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX_NAME = "Bearer ";
    public static final int AUTH_HEADER_VALUE_PREFIX_LENGTH = AUTH_HEADER_VALUE_PREFIX_NAME.length();

    public static String extractJwt(String authHeader) {
        return authHeader.substring(AUTH_HEADER_VALUE_PREFIX_LENGTH);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (isValidAuthHeader(authHeader)) {
            final String jwt = extractJwt(authHeader);
            try {
                final String email = JwtUtils.getEmail(jwt.strip());
                final String providerName = JwtUtils.getProvider(jwt.strip());
                if (StringUtils.hasLength(email) && StringUtils.hasLength(providerName)) {
                    Provider provider = Provider.valueOf(providerName);
                    Customer user = RepositoryUtils.getCustomer(customerRepository, email, provider);
                    if (!user.isEnabled()) throw new DisabledException("Disabled Account!");
                    if (!user.isAccountNonLocked()) throw new LockedException("Locked Account!");
                    if (JwtUtils.isValid(jwt, user)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (ExpiredJwtException exception) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String message = messageSourceUtils.getMessage("operation.failed.expired.jwt");
                ApiResponse apiResponse = new ApiResponse(false, message);
                new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                return;
            } catch (DecodingException exception) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String message = messageSourceUtils.getMessage("operation.failed.invalid.token.parsing");
                ApiResponse apiResponse = new ApiResponse(false, message);
                new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                return;
            } catch (SignatureException exception) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String message = messageSourceUtils.getMessage("operation.failed.invalid.token.signature");
                ApiResponse apiResponse = new ApiResponse(false, message);
                new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                return;
            } catch (AccountStatusException ex) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String message = messageSourceUtils.getMessage("operation.failed.disabled.or.locked.account");
                ApiResponse apiResponse = new ApiResponse(false, message);
                new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                return;
            } catch (CustomException exception) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String message = messageSourceUtils.getMessage("operation.failed.not.found.user");
                ApiResponse apiResponse = new ApiResponse(false, message);
                new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX_NAME);
    }
}
