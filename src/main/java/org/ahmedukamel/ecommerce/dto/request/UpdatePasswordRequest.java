package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.ahmedukamel.ecommerce.validation.Validator;

@Data
public class UpdatePasswordRequest {
    @NotBlank
    private String oldPassword;
    @NotNull
    @Pattern(regexp = Validator.PASSWORD_PATTERN)
    private String newPassword;
}
