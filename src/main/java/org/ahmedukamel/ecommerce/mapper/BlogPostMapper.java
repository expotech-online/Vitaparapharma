package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.BlogPostDto;
import org.ahmedukamel.ecommerce.dto.BlogPostDtoV2;
import org.ahmedukamel.ecommerce.model.BlogPost;
import org.ahmedukamel.ecommerce.model.BlogPostDetail;
import org.ahmedukamel.ecommerce.model.Like;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class BlogPostMapper {
    public static List<BlogPostDto> toResponse(Collection<BlogPost> items, Integer languageId) {
        return items.stream().map(item -> toResponse(item, languageId)).toList();
    }

    public static BlogPostDto toResponse(BlogPost post, Integer languageId) {
        BlogPostDetail blogPostDetail = EntityDetailsUtils.supplyBlogPostDetail(post, languageId);
        Integer likes = getLikes(post);
        Integer dislikes = getAllLikes(post) - likes;
        BlogPostDto response = new BlogPostDto();
        response.setTitle(blogPostDetail.getTitle());
        response.setContent(blogPostDetail.getContent());
        response.setBlogPostId(post.getBlogPostId());
        response.setPictureUrl(post.getPictureUrl());
        response.setDateCreated(post.getDateCreated().toString());
        response.setLikes(likes);
        response.setDislikes(dislikes);
        response.setLiked(false);
        response.setDisliked(false);
        return response;
    }

    private static Integer getLikes(BlogPost post) {
        return (int) post.getLikes().stream().filter(Like::getLike).count();
    }

    private static Integer getAllLikes(BlogPost post) {
        return post.getLikes().size();
    }

    public static List<BlogPostDto> toResponse(List<BlogPost> posts, Integer languageId, Integer customerId) {
        return posts.stream().map(item -> toResponse(item, languageId, customerId)).toList();
    }

    public static BlogPostDto toResponse(BlogPost post, Integer languageId, Integer customerId) {
        BlogPostDto response = toResponse(post, languageId);
        Optional<Like> customerLike = isLiked(post, customerId);
        response.setLiked(false);
        response.setDisliked(false);
        if (customerLike.isPresent()) {
            if (customerLike.get().getLike()) {
                response.setLiked(true);
            } else {
                response.setDisliked(true);
            }
        }
        return response;
    }

    private static Optional<Like> isLiked(BlogPost post, Integer customerId) {
        return post.getLikes().stream().filter(like -> like.getCustomer().getCustomerId().equals(customerId)).findFirst();
    }

    public static BlogPostDtoV2 toResponseV2(BlogPost post) {
        Function<String, BlogPostDetail> function = (s) -> post.getBlogPostDetails().stream()
                .filter(i -> i.getLanguage().getCode().equalsIgnoreCase(s.strip()))
                .findFirst().orElseGet(BlogPostDetail::new);

        BlogPostDetail en = function.apply("en"),
                ar = function.apply("ar"),
                fr = function.apply("fr");

        Function<Boolean, Long> likesFunction = (b) -> post.getLikes().stream()
                .filter(i -> i.getLike().equals(b)).count();

        BlogPostDtoV2 response = new BlogPostDtoV2();
        response.setTitle(en.getTitle());
        response.setContent(en.getContent());
        response.setArabicTitle(ar.getTitle());
        response.setArabicContent(ar.getContent());
        response.setFrenchTitle(fr.getTitle());
        response.setFrenchContent(fr.getContent());
        response.setBlogPostId(post.getBlogPostId());
        response.setDateCreated(post.getDateCreated().toString());
        response.setPictureUrl(post.getPictureUrl());
        response.setLikes(Math.toIntExact(likesFunction.apply(true)));
        response.setDislikes(Math.toIntExact(likesFunction.apply(false)));
        response.setLiked(false);
        response.setDisliked(false);
        return response;
    }
}
