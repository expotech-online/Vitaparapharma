package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ahmedukamel.ecommerce.validation.Validator;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChangePasswordRequest extends EmailCodeRequest {
    @NotBlank
    @Pattern(regexp = Validator.PASSWORD_PATTERN)
    private String password;
}
