package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.BlogPostDtoV4;
import org.ahmedukamel.ecommerce.model.BlogPost;
import org.ahmedukamel.ecommerce.model.BlogPostDetail;
import org.ahmedukamel.ecommerce.model.Like;
import org.ahmedukamel.ecommerce.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class BlogPostDtoV4Mapper implements Function<BlogPost, BlogPostDtoV4> {
    @Override
    public BlogPostDtoV4 apply(BlogPost post) {
        Function<String, BlogPostDetail> getDetails = languageCode -> post.getBlogPostDetails()
                .stream()
                .filter(productDetail -> productDetail.getLanguage().getCode().equalsIgnoreCase(languageCode))
                .findFirst().orElseGet(BlogPostDetail::new);

        Function<String, List<String>> getTags = languageCode -> post.getTags()
                .stream()
                .filter(tag -> tag.getLanguage().getCode().equalsIgnoreCase(languageCode))
                .map(Tag::getName)
                .toList();

        long reactions = post.getLikes().size(),
                likes = post.getLikes().stream().filter(Like::getLike).count();

        BlogPostDtoV4 dto = new BlogPostDtoV4();

        dto.setPicture(post.getPictureUrl());
        dto.setCreation(post.getDateCreated().toString());

        dto.setBlogPostId(post.getBlogPostId());
        dto.setLikes((int) likes);
        dto.setDislikes((int) (reactions - likes));

        BlogPostDetail details = getDetails.apply("en");
        dto.setEnglishTitle(details.getTitle());
        dto.setEnglishContent(details.getContent());

        details = getDetails.apply("ar");
        dto.setArabicTitle(details.getTitle());
        dto.setArabicContent(details.getContent());

        details = getDetails.apply("fr");
        dto.setFrenchTitle(details.getTitle());
        dto.setFrenchContent(details.getContent());

        dto.setEnglishTags(getTags.apply("en"));
        dto.setArabicTags(getTags.apply("ar"));
        dto.setFrenchTags(getTags.apply("fr"));

        return dto;
    }
}