package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class BlogPostDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    // Data for response
    private String pictureUrl;
    private String dateCreated;
    private Integer blogPostId;
    private Integer likes;
    private Integer dislikes;
    private Boolean liked;
    private Boolean disliked;
}