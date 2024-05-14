package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.blogPost.BlogPostService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/public/post")
public class PublicBlogPostController {
    private final BlogPostService service;

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getAllPosts() {
        ApiResponse response = service.getAllPosts();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "{postId}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable(value = "postId") Integer postId) {
        ApiResponse response = service.getPost(postId);
        return ResponseUtils.acceptedResponse(response);
    }
}