package org.ahmedukamel.ecommerce.config;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.model.enumeration.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccessDeniedHandlerConfig accessDeniedHandlerConfig;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("api/v*/auth/**", "api/v*/public/**", "/", "/login", "/privacy-policy", "p/**", "login/oauth2/**").permitAll()
                        .requestMatchers("api/v*/delivery/**").hasAnyAuthority(Role.DELIVERY.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers("api/v*/content/**").hasAnyAuthority(Role.CONTENT_CREATOR.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers("api/v*/admin/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers("api/v*/user/**").hasAnyAuthority(Role.CUSTOMER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers("api/v*/super/**").hasAuthority(Role.SUPER_ADMIN.name())
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth
//                        .loginProcessingUrl("/ecommerce/login/oauth2/code/{registrationId}")
//                        .loginProcessingUrl("/ecommerce-test/login/oauth2/code/{registrationId}")
//                        .loginProcessingUrl("https://{baseUrl}/login/oauth2/code/{registrationId}")
                        .successHandler(oAuth2SuccessHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandlerConfig))
        ;
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
