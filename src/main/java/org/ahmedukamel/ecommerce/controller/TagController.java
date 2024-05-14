package org.ahmedukamel.ecommerce.controller;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.tag.TagService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v4/public/tag")
public class TagController {
    final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTags() {
        ApiResponse response = service.getAllTags();
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "products")
    public ResponseEntity<ApiResponse> getProducts(@RequestBody TagsRequest request, @RequestParam int number) {
        ApiResponse response = service.getProducts(request.tags, number);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "posts")
    public ResponseEntity<ApiResponse> getPosts(@RequestBody TagsRequest request, @RequestParam int number) {
        ApiResponse response = service.getPosts(request.tags, number);
        return ResponseUtils.acceptedResponse(response);
    }

    record TagsRequest(List<String> tags) {
    }
}