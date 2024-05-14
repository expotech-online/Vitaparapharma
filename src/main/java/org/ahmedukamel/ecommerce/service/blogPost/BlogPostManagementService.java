package org.ahmedukamel.ecommerce.service.blogPost;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.BlogPostDtoV4;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.mapper.BlogPostDtoV4Mapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.repository.BlogPostRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.TagRepository;
import org.ahmedukamel.ecommerce.util.ImageDirectoryUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.ahmedukamel.ecommerce.util.EntityDetailsUtils.supplyBlogPostDetail;
import static org.ahmedukamel.ecommerce.util.ImageDirectoryUtils.deleteImage;
import static org.ahmedukamel.ecommerce.util.ImageDirectoryUtils.saveImage;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getLanguage;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogPostManagementService implements IBlogPostManagementService {
    final BlogPostRepository blogPostRepository;
    final LanguageRepository languageRepository;
    final TagRepository tagRepository;
    final BlogPostDtoV4Mapper mapper;

    @Override
    public ApiResponse addBlogPost(Object object, MultipartFile image) throws IOException {
        if (image.isEmpty()) throw new CustomException("Minimum allowed files is 1.");

        BlogPostDtoV4 request = (BlogPostDtoV4) object;
        BlogPost post = new BlogPost();

        updateBlogPost(post, request);
        updateTags(post, request);
        uploadImage(post, image);

        return new ApiResponse(true, "Blog post have been added successfully.");
    }

    @Override
    public ApiResponse updateBlogPost(int blogPostId, Object object) {
        BlogPostDtoV4 request = (BlogPostDtoV4) object;
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);

        updateBlogPost(post, request);
        updateTags(post, request);

        return new ApiResponse(true, "Blog post have been updated successfully.");
    }

    @Override
    public ApiResponse deleteBlogPost(int blogPostId) {
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        blogPostRepository.delete(post);
        return new ApiResponse(true, "Blog post have been deleted successfully.");
    }

    @Override
    public ApiResponse getBlogPost(int blogPostId) {
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        BlogPostDtoV4 response = mapper.apply(post);
        return new ApiResponse(true, "Blog post have been returned successfully.", Map.of("post", response));
    }

    @Override
    public ApiResponse getBlogPosts() {
        List<BlogPostDtoV4> responseList = blogPostRepository.findAll()
                .stream()
                .map(mapper)
                .toList();
        return new ApiResponse(true, "Blog post list have been returned successfully.", Map.of("posts", responseList));
    }

    @Override
    public ApiResponse uploadImage(int blogPostId, MultipartFile image) throws IOException {
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        if (StringUtils.hasLength(post.getPictureUrl())) {
            throw new CustomException("Blog post image is already exist.");
        }

        uploadImage(post, image);
        return new ApiResponse(true, "Blog post image have been uploaded successfully.");
    }

    @Override
    public ApiResponse removeImage(int blogPostId) {
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, blogPostId);
        if (!StringUtils.hasLength(post.getPictureUrl())) {
            throw new CustomException("Blog post image is not found.");
        }

        deleteImage(post.getPictureUrl());
        post.setPictureUrl(null);

        return new ApiResponse(true, "Product image deleted successfully.");
    }

    private void updateBlogPost(BlogPost post, BlogPostDtoV4 request) {
        Language enL = getLanguage(languageRepository, "en"),
                arL = getLanguage(languageRepository, "ar"),
                frL = getLanguage(languageRepository, "fr");

        BlogPostDetail en = supplyBlogPostDetail(post, enL.getLanguageId()),
                ar = supplyBlogPostDetail(post, arL.getLanguageId()),
                fr = supplyBlogPostDetail(post, frL.getLanguageId());

        en.setTitle(request.getEnglishTitle());
        en.setContent(request.getEnglishContent());
        en.setLanguage(enL);
        en.setBlogPost(post);

        ar.setTitle(request.getArabicTitle());
        ar.setContent(request.getArabicContent());
        ar.setLanguage(arL);
        ar.setBlogPost(post);

        fr.setTitle(request.getFrenchTitle());
        fr.setContent(request.getFrenchContent());
        fr.setLanguage(frL);
        fr.setBlogPost(post);

        post.getBlogPostDetails().add(en);
        post.getBlogPostDetails().add(ar);
        post.getBlogPostDetails().add(fr);

        blogPostRepository.save(post);
    }

    private void updateTags(BlogPost post, BlogPostDtoV4 request) {
        Language en = RepositoryUtils.getLanguage(languageRepository, "en"),
                ar = RepositoryUtils.getLanguage(languageRepository, "ar"),
                fr = RepositoryUtils.getLanguage(languageRepository, "fr");

        BiFunction<Collection<String>, Language, Collection<Tag>> getTags = (tags, language) -> tags
                .stream()
                .map(String::toLowerCase)
                .map(String::strip)
                .map(name -> {
                    Tag tag = tagRepository.findById(name).orElseGet(Tag::new);
                    tag.setName(name);
                    tag.setLanguage(language);
                    tag.getPosts().add(post);
                    return tagRepository.save(tag);
                })
                .toList();

        Collection<Tag> englishTags = getTags.apply(request.getEnglishTags(), en);
        Collection<Tag> arabicTags = getTags.apply(request.getArabicTags(), ar);
        Collection<Tag> frenchTags = getTags.apply(request.getFrenchTags(), fr);

        post.getTags().clear();
        post.getTags().addAll(englishTags);
        post.getTags().addAll(arabicTags);
        post.getTags().addAll(frenchTags);
        blogPostRepository.save(post);
    }

    private void uploadImage(BlogPost post, MultipartFile image) throws IOException {
        String imageName = saveImage(image);
        String imageUrl = ImageDirectoryUtils.getImageUrl(imageName);
        post.setPictureUrl(imageUrl);
        blogPostRepository.save(post);
    }
}