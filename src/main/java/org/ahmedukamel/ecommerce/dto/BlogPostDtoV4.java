package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BlogPostDtoV4 {
    @NotBlank
    private String englishTitle;
    @NotBlank
    private String arabicTitle;
    @NotBlank
    private String frenchTitle;
    @NotBlank
    private String englishContent;
    @NotBlank
    private String arabicContent;
    @NotBlank
    private String frenchContent;
    private String picture;
    private String creation;
    private int blogPostId;
    private int likes;
    private int dislikes;
    private boolean liked;
    private boolean disliked;
    private List<String> englishTags;
    private List<String> arabicTags;
    private List<String> frenchTags;
}