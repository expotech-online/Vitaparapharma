package org.ahmedukamel.ecommerce.service.blogPost;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.BlogPostDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.BlogPostMapper;
import org.ahmedukamel.ecommerce.model.BlogPost;
import org.ahmedukamel.ecommerce.model.BlogPostDetail;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.repository.BlogPostRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.util.ImageDirectoryUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.ahmedukamel.ecommerce.util.EntityDetailsUtils.supplyBlogPostDetail;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getLanguage;

@Service
@RequiredArgsConstructor
public class BlogPostServiceV2Impl implements BlogPostServiceV2 {
    final BlogPostRepository blogPostRepository;
    final LanguageRepository languageRepository;
    final MessageSourceUtils messageSourceUtils;

    @Override
    public ApiResponse addBlogPost(BlogPostDtoV2 request, MultipartFile image) throws IOException {
        BlogPost post = new BlogPost();
        setBlogPostValues(post, request);
        String imageName = ImageDirectoryUtils.saveImage(image);
        post.setPictureUrl(ImageDirectoryUtils.getImageUrl(imageName));
        blogPostRepository.save(post);
        version3(post, request);
        String message = messageSourceUtils.getMessage("operation.successful.add.post");
        return new ApiResponse(true, message);
    }

    protected void version3(BlogPost post, BlogPostDtoV2 request) {
    }

    @Transactional
    @Override
    public ApiResponse updateBlogPost(BlogPostDtoV2 request, Integer id) {
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, id);
        setBlogPostValues(post, request);
        blogPostRepository.save(post);
        version3(post, request);
        String message = messageSourceUtils.getMessage("operation.successful.update.post");
        return new ApiResponse(true, message);
    }

    @Transactional
    @Override
    public ApiResponse getBlogPostById(int id) {
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, id);
        BlogPostDtoV2 response = BlogPostMapper.toResponseV2(post);
        String message = messageSourceUtils.getMessage("operation.successful.get.post");
        return new ApiResponse(true, message, Map.of("post", response));
    }

    private void setBlogPostValues(BlogPost post, BlogPostDtoV2 request) {
        Language enL = getLanguage(languageRepository, "en"), arL = getLanguage(languageRepository, "ar"), frL = getLanguage(languageRepository, "fr");

        BlogPostDetail en = supplyBlogPostDetail(post, enL.getLanguageId()), ar = supplyBlogPostDetail(post, arL.getLanguageId()), fr = supplyBlogPostDetail(post, frL.getLanguageId());

        en.setTitle(request.getTitle());
        en.setContent(request.getContent());
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

        post.getBlogPostDetails().addAll(List.of(en, ar, fr));
    }
}