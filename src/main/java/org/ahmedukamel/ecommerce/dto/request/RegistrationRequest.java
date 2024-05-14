package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import lombok.EqualsAndHashCode;
import org.ahmedukamel.ecommerce.validation.Validator;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegistrationRequest extends LoginRequest {
    @NotBlank
    @Pattern(regexp = Validator.PHONE_PATTERN)
    private String phone;
}
