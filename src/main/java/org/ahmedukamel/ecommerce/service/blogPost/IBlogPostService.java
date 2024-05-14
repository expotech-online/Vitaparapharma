package org.ahmedukamel.ecommerce.service.blogPost;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

public interface IBlogPostService {
    ApiResponse getBlogPostById(int blogPostId);

    ApiResponse getAllBlogPosts();

    ApiResponse getBlogPostByIdForUser(int blogPostId);

    ApiResponse getAllBlogPostsForUser();
}