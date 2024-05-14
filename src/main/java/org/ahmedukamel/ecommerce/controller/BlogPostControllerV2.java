package org.ahmedukamel.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ahmedukamel.ecommerce.dto.BlogPostDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.blogPost.BlogPostServiceV2;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "api/v2/content/post")
@Validated
public class BlogPostControllerV2 {
    private final BlogPostServiceV2 service;

    public BlogPostControllerV2(@Qualifier("blogPostServiceV2Impl") BlogPostServiceV2 service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addBlogPost(
            @Valid @ModelAttribute(value = "post") BlogPostDtoV2 request,
            @NotNull @ModelAttribute(value = "image") MultipartFile image)
            throws IOException {
        ApiResponse response = service.addBlogPost(request, image);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{postId}")
    public ResponseEntity<ApiResponse> updateBlogPost(@PathVariable(value = "postId") Integer postId, @RequestBody BlogPostDtoV2 request) {
        ApiResponse response = service.updateBlogPost(request, postId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "{postId}")
    public ResponseEntity<ApiResponse> getBlogPostById(@PathVariable(value = "postId") int postId) {
        ApiResponse response = service.getBlogPostById(postId);
        return ResponseUtils.acceptedResponse(response);
    }

    @Component
    @RequiredArgsConstructor
    public static class StringToBlogPostDtoV2Converter implements Converter<String, BlogPostDtoV2> {
        final ObjectMapper objectMapper;

        @SneakyThrows
        @Override
        public BlogPostDtoV2 convert(@NonNull String source) {
            return objectMapper.readValue(source, BlogPostDtoV2.class);
        }
    }
}
