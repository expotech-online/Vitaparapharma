package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.BlogPostDtoV3;
import org.ahmedukamel.ecommerce.model.BlogPost;
import org.ahmedukamel.ecommerce.model.BlogPostDetail;
import org.ahmedukamel.ecommerce.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class BlogPostMapperV4 implements BiFunction<BlogPost, String, BlogPostDtoV3> {
    @Override
    public BlogPostDtoV3 apply(BlogPost post, String languageCode) {
        Function<String, BlogPostDetail> function = (s) -> post.getBlogPostDetails().stream()
                .filter(i -> i.getLanguage().getCode().equalsIgnoreCase(s.strip()))
                .findFirst().orElseGet(BlogPostDetail::new);

        BlogPostDetail detail = function.apply(languageCode);

        Function<Boolean, Long> likesFunction = (b) -> post.getLikes().stream()
                .filter(i -> i.getLike().equals(b)).count();

        BlogPostDtoV3 response = new BlogPostDtoV3();
        response.setTitle(detail.getTitle());
        response.setContent(detail.getContent());
        response.setBlogPostId(post.getBlogPostId());
        response.setDateCreated(post.getDateCreated().toString());
        response.setPictureUrl(post.getPictureUrl());
        response.setLikes(Math.toIntExact(likesFunction.apply(true)));
        response.setDislikes(Math.toIntExact(likesFunction.apply(false)));
        response.setLiked(false);
        response.setDisliked(false);
        response.setTags(post.getTags().stream().map(Tag::getName).toList());
        return response;
    }
}
