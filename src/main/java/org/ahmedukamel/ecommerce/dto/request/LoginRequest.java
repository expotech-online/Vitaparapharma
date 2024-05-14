package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.ahmedukamel.ecommerce.validation.Validator;

@Data
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = Validator.PASSWORD_PATTERN)
    private String password;
    public boolean rememberMe;
    private String deviceToken;
}
