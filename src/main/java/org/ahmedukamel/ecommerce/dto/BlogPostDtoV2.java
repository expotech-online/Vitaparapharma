package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogPostDtoV2 extends BlogPostDto {
    @NotBlank
    private String arabicTitle;
    @NotBlank
    private String arabicContent;
    @NotBlank
    private String frenchTitle;
    @NotBlank
    private String frenchContent;
}