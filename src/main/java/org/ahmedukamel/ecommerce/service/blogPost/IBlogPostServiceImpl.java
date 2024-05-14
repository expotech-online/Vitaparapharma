package org.ahmedukamel.ecommerce.service.blogPost;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.BlogPostResponse;
import org.ahmedukamel.ecommerce.mapper.BlogPostResponseMapper;
import org.ahmedukamel.ecommerce.model.BlogPost;
import org.ahmedukamel.ecommerce.model.Like;
import org.ahmedukamel.ecommerce.repository.BlogPostRepository;
import org.ahmedukamel.ecommerce.util.LocaleContextUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.SecurityContextUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class IBlogPostServiceImpl implements IBlogPostService {
    final BlogPostResponseMapper mapper;
    final BlogPostRepository blogPostRepository;

    @Override
    public ApiResponse getBlogPostById(int blogPostId) {
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        BlogPostResponse response = mapper.apply(post, LocaleContextUtils.getLanguage());
        return new ApiResponse(true, "Blog post have been returned successfully.", Map.of("post", response));
    }

    @Override
    public ApiResponse getAllBlogPosts() {
        List<BlogPostResponse> responseList = blogPostRepository.findAll()
                .stream()
                .map(post -> mapper.apply(post, LocaleContextUtils.getLanguage()))
                .toList();
        return new ApiResponse(true, "Blog posts have been returned successfully.", Map.of("posts", responseList));
    }

    @Override
    public ApiResponse getBlogPostByIdForUser(int blogPostId) {
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        BlogPostResponse response = getResponse(post);
        return new ApiResponse(true, "Blog post have been returned successfully.", Map.of("post", response));
    }

    @Override
    public ApiResponse getAllBlogPostsForUser() {
        List<BlogPostResponse> responseList = blogPostRepository.findAll()
                .stream()
                .map(this::getResponse)
                .toList();
        return new ApiResponse(true, "Blog posts have been returned successfully.", Map.of("posts", responseList));
    }

    public BlogPostResponse getResponse(BlogPost post) {
        int customerId = SecurityContextUtils.getPrinciple().getCustomerId();
        BlogPostResponse response = mapper.apply(post, LocaleContextUtils.getLanguage());
        Optional<Like> optionalLike = post.getLikes()
                .stream()
                .filter(like -> like.getCustomer().getCustomerId().equals(customerId))
                .findFirst();
        optionalLike.ifPresent(like -> {
            if (like.getLike()) response.setLiked(true);
            else response.setDisliked(true);
        });
        return response;
    }
}