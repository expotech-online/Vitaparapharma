package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.response.BlogPostResponse;
import org.ahmedukamel.ecommerce.model.*;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Predicate;

@Component
public class BlogPostResponseMapper implements BiFunction<BlogPost, String, BlogPostResponse> {
    @Override
    public BlogPostResponse apply(BlogPost post, String languageCode) {
        BlogPostDetail details = post.getBlogPostDetails()
                .stream()
                .filter(productDetail -> productDetail.getLanguage().getCode().equalsIgnoreCase(languageCode))
                .findFirst()
                .orElseGet(BlogPostDetail::new);

        Predicate<Tag> tagFilter = tag -> tag.getLanguage().getCode().equalsIgnoreCase(languageCode);

        long reactions = post.getLikes().size(),
                likes = post.getLikes().stream().filter(Like::getLike).count();

        BlogPostResponse response = new BlogPostResponse();

        response.setPicture(post.getPictureUrl());
        response.setCreation(post.getDateCreated().toString());

        response.setBlogPostId(post.getBlogPostId());
        response.setLikes((int) likes);
        response.setDislikes((int) (reactions - likes));

        response.setTitle(details.getTitle());
        response.setContent(details.getContent());

        response.setTags(post.getTags()
                .stream()
                .filter(tagFilter)
                .map(Tag::getName)
                .toList()
        );

        return response;
    }
}