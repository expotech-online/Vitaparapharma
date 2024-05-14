package org.ahmedukamel.ecommerce.service.blogPost;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.BlogPostDto;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.BlogPostMapper;
import org.ahmedukamel.ecommerce.model.BlogPost;
import org.ahmedukamel.ecommerce.model.BlogPostDetail;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.repository.BlogPostRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.service.blogPost.BlogPostService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostRepository blogPostRepository;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;

    @Override
    public ApiResponse addPost(BlogPostDto request) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        BlogPost post = new BlogPost();
        post.setPictureUrl("");
        BlogPostDetail blogPostDetail = new BlogPostDetail();
        blogPostDetail.setBlogPost(post);
        blogPostDetail.setLanguage(language);
        post.getBlogPostDetails().add(blogPostDetail);
        UpdateUtils.updatePostDetails(request, blogPostDetail);
        blogPostRepository.save(post);
        BlogPostDto response = BlogPostMapper.toResponse(post, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.post");
        return new ApiResponse(true, message, Map.of("post", response));
    }

    @Override
    public ApiResponse updatePost(BlogPostDto request, Integer postId) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, postId);
        BlogPostDetail blogPostDetail = EntityDetailsUtils.supplyBlogPostDetail(post, language.getLanguageId());
        blogPostDetail.setBlogPost(post);
        blogPostDetail.setLanguage(language);
        post.getBlogPostDetails().add(blogPostDetail);
        UpdateUtils.updatePostDetails(request, blogPostDetail);
        blogPostRepository.save(post);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.post");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse deletePost(Integer postId) {
        // Querying
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, postId);
        // Processing
        ImageDirectoryUtils.deleteImage(post.getPictureUrl());
        blogPostRepository.delete(post);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.post");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getPost(Integer blogPostId) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        BlogPostDto response = BlogPostMapper.toResponse(post, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.post");
        return new ApiResponse(true, message, Map.of("post", response));
    }

    @Override
    public ApiResponse getAllPosts() {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        List<BlogPost> posts = blogPostRepository.findAll();
        // Processing
        List<BlogPostDto> blogPostDtoList = BlogPostMapper.toResponse(posts, language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.posts");
        return new ApiResponse(true, message, Map.of("posts", blogPostDtoList));
    }

    @Override
    public ApiResponse uploadPostPicture(Integer blogPostId, MultipartFile image) throws IOException {
        // Querying
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        // Validation
        ValidationUtils.validateImage(image, messageSourceUtils);
        ValidationUtils.validateNotExistPostPicture(post, messageSourceUtils);
        // Processing
        String imageName = ImageDirectoryUtils.saveImage(image);
        post.setPictureUrl(ImageDirectoryUtils.getImageUrl(imageName));
        blogPostRepository.save(post);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.aad.post.picture");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse deletePostPicture(Integer blogPostId) {
        // Querying
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        // Validation
        ValidationUtils.validateExistPostPicture(post, messageSourceUtils);
        // Processing
        ImageDirectoryUtils.deleteImage(post.getPictureUrl());
        post.setPictureUrl("");
        blogPostRepository.save(post);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.aad.post.picture");
        return new ApiResponse(true, message);
    }
}