package org.ahmedukamel.ecommerce.service.blogPost;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IBlogPostManagementService {
    ApiResponse addBlogPost(Object object, MultipartFile image) throws IOException;

    ApiResponse updateBlogPost(int blogPostId, Object object);

    ApiResponse deleteBlogPost(int blogPostId);

    ApiResponse getBlogPost(int blogPostId);

    ApiResponse getBlogPosts();

    ApiResponse uploadImage(int blogPostId, MultipartFile image) throws IOException;

    ApiResponse removeImage(int blogPostId);
}