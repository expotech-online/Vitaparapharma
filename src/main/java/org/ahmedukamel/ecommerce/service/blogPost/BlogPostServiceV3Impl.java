package org.ahmedukamel.ecommerce.service.blogPost;

import org.ahmedukamel.ecommerce.dto.BlogPostDtoV2;
import org.ahmedukamel.ecommerce.dto.BlogPostDtoV3;
import org.ahmedukamel.ecommerce.model.BlogPost;
import org.ahmedukamel.ecommerce.model.Tag;
import org.ahmedukamel.ecommerce.repository.BlogPostRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.TagRepository;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogPostServiceV3Impl extends BlogPostServiceV2Impl {

    final TagRepository tagRepository;

    public BlogPostServiceV3Impl(BlogPostRepository blogPostRepository,
                                 LanguageRepository languageRepository,
                                 MessageSourceUtils messageSourceUtils,
                                 TagRepository tagRepository) {
        super(blogPostRepository, languageRepository, messageSourceUtils);
        this.tagRepository = tagRepository;
    }

    @Override
    protected void version3(BlogPost post, BlogPostDtoV2 request) {
        BlogPostDtoV3 requestV3 = (BlogPostDtoV3) request;
        post.getTags().clear();
        Set<Tag> tags = requestV3.getTags()
                .stream().map(String::toLowerCase)
                .map(String::strip)
                .map(name -> {
                    Tag tag = tagRepository.findById(name).orElse(new Tag(name));
                    tag.getPosts().add(post);
                    return tagRepository.save(tag);
                })
                .collect(Collectors.toSet());
        post.getTags().addAll(tags);
        blogPostRepository.save(post);
    }
}