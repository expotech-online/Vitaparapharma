package org.ahmedukamel.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ahmedukamel.ecommerce.dto.BlogPostDtoV4;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.blogPost.IBlogPostManagementService;
import org.ahmedukamel.ecommerce.service.blogPost.IBlogPostService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "api/v4")
@Validated
public class BlogPostControllerV4 {
    final IBlogPostManagementService managementService;
    final IBlogPostService service;

    public BlogPostControllerV4(IBlogPostManagementService managementService, IBlogPostService service) {
        this.managementService = managementService;
        this.service = service;
    }

    @PostMapping(value = "content/post")
    public ResponseEntity<ApiResponse> addBlogPost(@Valid @RequestParam(value = "post") BlogPostDtoV4 request,
                                                   @RequestParam(value = "image") MultipartFile image) throws IOException {
        ApiResponse response = managementService.addBlogPost(request, image);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "content/post/{postId}")
    public ResponseEntity<ApiResponse> updateBlogPost(@PathVariable(value = "postId") Integer postId,
                                                      @Valid @RequestBody BlogPostDtoV4 request) {
        ApiResponse response = managementService.updateBlogPost(postId, request);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "content/post/{postId}")
    public ResponseEntity<ApiResponse> getBlogPost(@PathVariable(value = "postId") int postId) {
        ApiResponse response = managementService.getBlogPost(postId);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "content/post/{postId}")
    public ResponseEntity<ApiResponse> deleteBlogPost(@PathVariable(value = "postId") int postId) {
        ApiResponse response = managementService.deleteBlogPost(postId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "content/post/all")
    public ResponseEntity<ApiResponse> getBlogPosts() {
        ApiResponse response = managementService.getBlogPosts();
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "content/post/{postId}/upload")
    public ResponseEntity<ApiResponse> uploadImage(@PathVariable(value = "postId") int postId,
                                                   @RequestParam(value = "image") MultipartFile image) throws IOException {
        ApiResponse response = managementService.uploadImage(postId, image);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "content/post/{postId}/remove")
    public ResponseEntity<ApiResponse> removeImage(@PathVariable(value = "postId") int postId) {
        ApiResponse response = managementService.removeImage(postId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/post/all")
    public ResponseEntity<ApiResponse> getAllBlogPosts() {
        ApiResponse response = service.getAllBlogPosts();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/post/{postId}")
    public ResponseEntity<ApiResponse> getBlogPostById(@PathVariable(value = "postId") Integer postId) {
        ApiResponse response = service.getBlogPostById(postId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/post/all")
    public ResponseEntity<ApiResponse> getAllBlogPostsForUser() {
        ApiResponse response = service.getAllBlogPostsForUser();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/post/{postId}")
    public ResponseEntity<ApiResponse> getBlogPostByIdForUser(@PathVariable(value = "postId") Integer postId) {
        ApiResponse response = service.getBlogPostByIdForUser(postId);
        return ResponseUtils.acceptedResponse(response);
    }

    @Component
    @RequiredArgsConstructor
    public static class StringBlogPostDtoV4Converter implements Converter<String, BlogPostDtoV4> {
        final ObjectMapper objectMapper;

        @SneakyThrows
        @Override
        public BlogPostDtoV4 convert(@NonNull String source) {
            return objectMapper.readValue(source, BlogPostDtoV4.class);
        }
    }
}