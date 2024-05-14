package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BlogPostResponse {
    private String title;
    private String content;
    private String picture;
    private String creation;
    private Integer blogPostId;
    private Integer likes;
    private Integer dislikes;
    private Boolean liked;
    private Boolean disliked;
    private List<String> tags;
}