package org.ahmedukamel.ecommerce.service.blogPost;

import org.ahmedukamel.ecommerce.dto.BlogPostDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BlogPostServiceV2 {
    ApiResponse addBlogPost(BlogPostDtoV2 request, MultipartFile image) throws IOException;

    ApiResponse updateBlogPost(BlogPostDtoV2 request, Integer id);

    ApiResponse getBlogPostById(int blogPostId);
}
